package com.example.nutrismart.domain.usecase.dailyideas

import com.example.nutrismart.domain.ai.model.SmartAiRequest
import com.example.nutrismart.domain.ai.model.toRecipe
import com.example.nutrismart.domain.model.DailyIdea
import com.example.nutrismart.domain.model.MoodType
import com.example.nutrismart.domain.model.User
import com.example.nutrismart.domain.repository.RecipeRepository
import com.example.nutrismart.domain.service.IntelligentRecipeService

/**
 * Advanced UseCase that orchestrates Intelligent Recipe Generation
 */
class GenerateAiDailyIdeasUseCase(
    private val aiRecipeService: IntelligentRecipeService,
    private val fallbackUseCase: GenerateMoodBasedDailyIdeasUseCase,
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(
        mood: MoodType,
        user: User?,
        useAi: Boolean = true
    ): List<DailyIdea> {
        val dietType = user?.dietCategory ?: "Balanced"
        val budget = if ((user?.budget ?: 0) < 300) "Low" else "Mid"
        val maxTime = user?.maxTime ?: 45

        if (useAi) {
            val request = SmartAiRequest(
                mood = mood.displayName,
                dietCategory = dietType,
                budgetLevel = budget,
                maxTimeMinutes = maxTime,
                preferTunisian = true,
                mealType = "Lunch"
            )

            val result = aiRecipeService.generateSmartRecipe(request)

            if (result.isSuccess) {
                val aiRecipe = result.getOrThrow().toRecipe()
                val aiIdea = DailyIdea(
                    recipe = aiRecipe,
                    moodTitle = "✨ AI Magic: ${aiRecipe.title}"
                )
                
                // Mix with some fallback recipes for a full list
                val allRecipes = recipeRepository.getAllRecipes()
                val fallbacks = fallbackUseCase(allRecipes, mood, user)
                
                return listOf(aiIdea) + fallbacks.take(4)
            }
        }

        // Fallback if AI is disabled or fails (though Service itself has a fallback)
        val allRecipes = recipeRepository.getAllRecipes()
        return fallbackUseCase(allRecipes, mood, user)
    }
}

