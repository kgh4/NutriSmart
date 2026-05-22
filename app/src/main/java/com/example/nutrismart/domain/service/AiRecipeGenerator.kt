package com.example.nutrismart.domain.service

import com.example.nutrismart.domain.model.Recipe

interface AiRecipeGenerator {
    suspend fun generateRecipe(
        mood: String,
        dietType: String,
        budget: String,
        maxTime: Int,
        ingredients: List<String> = emptyList()
    ): Result<AiGeneratedRecipe>
}

data class AiGeneratedRecipe(
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val estimatedTime: Int,
    val budgetLevel: String,
    val calories: Int,
    val whyThisFitsYou: String = ""
)

fun AiGeneratedRecipe.toRecipe(): Recipe {
    return Recipe(
        id = "ai_${System.currentTimeMillis()}_${title.hashCode()}",
        title = title,
        description = description,
        ingredients = ingredients.joinToString("\n"),
        steps = steps.joinToString("\n"),
        time = estimatedTime,
        budget = budgetLevel,
        calories = calories,
        dietCategory = "AI Generated"
    )
}
