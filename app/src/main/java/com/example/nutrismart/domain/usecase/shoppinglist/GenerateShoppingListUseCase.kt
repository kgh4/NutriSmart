package com.example.nutrismart.domain.usecase.shoppinglist

import com.example.nutrismart.domain.generator.DietRecipeProvider
import com.example.nutrismart.domain.generator.IngredientPriceProvider
import com.example.nutrismart.domain.model.ShoppingItem
import com.example.nutrismart.domain.model.ShoppingList
import com.example.nutrismart.domain.repository.ShoppingListRepository
import com.example.nutrismart.domain.repository.MealPlanRepository
import java.time.LocalDateTime
import java.util.UUID

class GenerateShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val dietRecipeProvider: DietRecipeProvider = DietRecipeProvider(),
    private val priceProvider: IngredientPriceProvider = IngredientPriceProvider()
) {
    suspend operator fun invoke(weeklyMealPlanId: String): Result<ShoppingList> {
        return try {
            val mealPlan = mealPlanRepository.getActiveMealPlan()
                ?: return Result.failure(Exception("Please select a plan first in the Weekly Planner."))

            val allItems = mutableListOf<ShoppingItem>()
            
            for (day in mealPlan.days) {
                val slots = listOf(day.breakfast, day.lunch, day.dinner, day.snack)
                for (slot in slots) {
                    val recipe = slot.recipe ?: slot.recipeId?.let { dietRecipeProvider.getRecipeById(it) }
                    
                    recipe?.let { r ->
                        // Improved parsing: handle simple quantities if present (e.g., "200g Chicken")
                        r.ingredients.split(",").forEach { ingredientStr ->
                            val trimmed = ingredientStr.trim()
                            if (trimmed.isNotEmpty()) {
                                val (name, quantity, unit) = parseIngredient(trimmed)
                                val unitPrice = priceProvider.getPrice(name) / 1000.0
                                allItems.add(
                                    ShoppingItem(
                                        id = UUID.randomUUID().toString(),
                                        name = name,
                                        quantity = quantity,
                                        unit = unit,
                                        checked = false,
                                        category = detectCategory(name),
                                        price = unitPrice,
                                        totalCost = unitPrice * quantity
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Consolidate duplicates case-insensitively
            val consolidatedItems = allItems.groupBy { it.name.lowercase() }.map { (lowerName, items) ->
                val firstName = items.first().name
                val totalQuantity = items.sumOf { it.quantity }
                val commonUnit = items.first().unit
                val commonCategory = items.first().category
                val unitPrice = items.first().price
                
                ShoppingItem(
                    id = UUID.randomUUID().toString(),
                    name = firstName.replaceFirstChar { it.uppercase() },
                    quantity = totalQuantity,
                    unit = commonUnit,
                    checked = false,
                    category = commonCategory,
                    price = unitPrice,
                    totalCost = unitPrice * totalQuantity
                )
            }

            val shoppingList = ShoppingList(
                id = UUID.randomUUID().toString(),
                mealPlanId = mealPlan.id, // Use the active plan's ID
                items = consolidatedItems,
                createdAt = LocalDateTime.now()
            )

            shoppingListRepository.saveShoppingList(shoppingList)
            Result.success(shoppingList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun parseIngredient(input: String): Triple<String, Double, String> {
        // Simple heuristic: "200g Chicken" or "2 Chicken" or just "Chicken"
        val parts = input.split(" ")
        if (parts.size > 1) {
            val firstPart = parts[0].filter { it.isDigit() || it == '.' }.toDoubleOrNull()
            if (firstPart != null) {
                val unit = parts[0].filter { it.isLetter() }.ifEmpty { "unit" }
                val name = parts.drop(1).joinToString(" ")
                return Triple(name, firstPart, unit)
            }
        }
        return Triple(input, 1.0, "unit")
    }

    private fun detectCategory(name: String): String {
        val lower = name.lowercase()
        return when {
            listOf("tomato", "onion", "garlic", "carrot", "broccoli", "spinach", "potato", "pepper").any { lower.contains(it) } -> "Vegetables"
            listOf("chicken", "beef", "pork", "fish", "egg", "tofu", "turkey", "salmon").any { lower.contains(it) } -> "Protein"
            listOf("milk", "cheese", "butter", "yogurt", "cream").any { lower.contains(it) } -> "Dairy"
            listOf("rice", "pasta", "bread", "flour", "quinoa", "oats").any { lower.contains(it) } -> "Grains"
            listOf("salt", "pepper", "oil", "sugar", "sauce", "spice", "herb").any { lower.contains(it) } -> "Spices/Pantry"
            else -> "Other"
        }
    }
}
