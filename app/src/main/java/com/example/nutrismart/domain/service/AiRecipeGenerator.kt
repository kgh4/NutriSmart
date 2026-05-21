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
    val name: String,
    val shortDescription: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val prepMinutes: Int,
    val estimatedBudget: String,
    val moodTag: String,
    val whyThisFitsYou: String
)

fun AiGeneratedRecipe.toRecipe(): Recipe {
    return Recipe(
        id = "ai_${System.currentTimeMillis()}",
        title = name,
        description = whyThisFitsYou,
        ingredients = ingredients.joinToString("\n"),
        steps = steps.joinToString("\n"),
        time = prepMinutes,
        budget = estimatedBudget,
        dietCategory = "AI Generated"
    )
}
