package com.example.nutrismart.domain.ai.model

import com.example.nutrismart.domain.model.Recipe

/**
 * Request model for advanced AI recipe generation
 */
data class SmartAiRequest(
    val mood: String = "",
    val dietCategory: String = "Balanced",
    val budgetLevel: String = "Medium",
    val maxTimeMinutes: Int = 45,
    val availableIngredients: List<String> = emptyList(),
    val leftoverItems: List<String> = emptyList(),
    val allergies: List<String> = emptyList(),
    val dislikes: List<String> = emptyList(),
    val mealType: String = "Lunch", // Breakfast, Lunch, Dinner, Snack
    val cookingSkill: String = "Medium",
    val preferTunisian: Boolean = true
)

/**
 * High-fidelity response model from AI
 */
data class SmartAiResponse(
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val mealType: String,
    val calories: Int,
    val timeMinutes: Int,
    val budgetLevel: String,
    val dietCategory: String,
    val estimatedCost: Double,
    val difficulty: String,
    val substitutions: Map<String, String>,
    val whyItFits: String,
    val shoppingItems: List<String>,
    val warnings: List<String>
)

/**
 * Mapper to convert rich AI response to standard app Recipe model
 */
fun SmartAiResponse.toRecipe(): Recipe {
    return Recipe(
        id = "ai_${System.currentTimeMillis()}",
        title = title,
        description = whyItFits,
        ingredients = ingredients.joinToString("\n"),
        steps = steps.joinToString("\n"),
        mealType = mealType,
        time = timeMinutes,
        calories = calories,
        budget = budgetLevel,
        dietCategory = dietCategory
    )
}
