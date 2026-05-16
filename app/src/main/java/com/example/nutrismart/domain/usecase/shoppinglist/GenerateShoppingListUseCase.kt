package com.example.nutrismart.domain.usecase.shoppinglist

import com.example.nutrismart.domain.generator.DietRecipeProvider
import com.example.nutrismart.domain.model.ShoppingItem
import com.example.nutrismart.domain.model.ShoppingList
import com.example.nutrismart.domain.repository.ShoppingListRepository
import com.example.nutrismart.domain.repository.MealPlanRepository
import java.time.LocalDateTime
import java.util.UUID

class GenerateShoppingListUseCase(
    private val shoppingListRepository: ShoppingListRepository,
    private val mealPlanRepository: MealPlanRepository,
    private val dietRecipeProvider: DietRecipeProvider = DietRecipeProvider()
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
                        r.ingredients.split("\n").forEach { ingredient ->
                            if (ingredient.isNotBlank()) {
                                allItems.add(
                                    ShoppingItem(
                                        id = UUID.randomUUID().toString(),
                                        name = ingredient.trim(),
                                        quantity = "1 unit",
                                        checked = false,
                                        category = detectCategory(ingredient)
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // Consolidate duplicates
            val consolidatedItems = allItems.groupBy { it.name.lowercase() }.map { (lowerName, items) ->
                val firstName = items.first().name
                val totalCount = items.size
                
                ShoppingItem(
                    id = UUID.randomUUID().toString(),
                    name = firstName.replaceFirstChar { it.uppercase() },
                    quantity = if (totalCount > 1) "$totalCount units" else "1 unit",
                    checked = false,
                    category = items.first().category
                )
            }

            val shoppingList = ShoppingList(
                id = UUID.randomUUID().toString(),
                mealPlanId = mealPlan.id,
                items = consolidatedItems,
                createdAt = LocalDateTime.now()
            )

            shoppingListRepository.saveShoppingList(shoppingList)
            Result.success(shoppingList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun detectCategory(name: String): String {
        val lower = name.lowercase()
        return when {
            listOf("tomato", "onion", "garlic", "carrot", "broccoli", "spinach", "potato", "pepper", "avocado", "cucumber", "lettuce", "veggie", "vegetable").any { lower.contains(it) } -> "Produce"
            listOf("chicken", "beef", "pork", "fish", "egg", "tofu", "turkey", "salmon", "tuna", "steak").any { lower.contains(it) } -> "Proteins"
            listOf("milk", "cheese", "butter", "yogurt", "cream", "feta").any { lower.contains(it) } -> "Dairy"
            listOf("rice", "pasta", "bread", "flour", "quinoa", "oats", "tortilla", "wrap").any { lower.contains(it) } -> "Grains/Pantry"
            else -> "Other"
        }
    }
}
