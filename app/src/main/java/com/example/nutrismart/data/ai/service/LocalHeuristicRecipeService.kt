package com.example.nutrismart.data.ai.service

import com.example.nutrismart.domain.ai.model.SmartAiRequest
import com.example.nutrismart.domain.ai.model.SmartAiResponse
import com.example.nutrismart.domain.repository.RecipeRepository
import com.example.nutrismart.domain.service.IntelligentRecipeService

/**
 * Local fallback service that returns the best match from the local DB.
 * Ensures the system works without internet or API keys.
 */
class LocalHeuristicRecipeService(
    private val recipeRepository: RecipeRepository
) : IntelligentRecipeService {

    override suspend fun generateSmartRecipe(request: SmartAiRequest): Result<SmartAiResponse> = runCatching {
        val allRecipes = recipeRepository.getAllRecipes()
        
        // Find a recipe that matches diet and is within budget/time
        val match = allRecipes.firstOrNull { 
            it.dietCategory.equals(request.dietCategory, ignoreCase = true) &&
            it.time <= request.maxTimeMinutes
        } ?: allRecipes.first()

        SmartAiResponse(
            title = match.title,
            description = match.description,
            ingredients = match.ingredients.split("\n"),
            steps = match.steps.split("\n"),
            mealType = match.mealType,
            calories = match.calories,
            timeMinutes = match.time,
            budgetLevel = match.budget,
            dietCategory = match.dietCategory,
            estimatedCost = 0.0,
            difficulty = "Medium",
            substitutions = emptyMap(),
            whyItFits = "Found a local favorite matching your ${request.dietCategory} preference.",
            shoppingItems = emptyList(),
            warnings = listOf("Local database fallback")
        )
    }
}
