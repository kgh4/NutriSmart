package com.example.nutrismart.domain.usecase.dailyideas

import android.util.Log
import com.example.nutrismart.domain.model.DailyIdea
import com.example.nutrismart.domain.model.MoodType
import com.example.nutrismart.domain.model.User
import com.example.nutrismart.domain.repository.RecipeRepository
import com.example.nutrismart.domain.service.AiRecipeGenerator
import com.example.nutrismart.domain.service.toRecipe
import com.example.nutrismart.data.ai.CerebrasRecipeGenerator

class GenerateAiDailyIdeasUseCase(
    private val aiRecipeGenerator: AiRecipeGenerator,
    private val fallbackUseCase: GenerateMoodBasedDailyIdeasUseCase,
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(
        mood: MoodType,
        user: User?,
        useAi: Boolean = true
    ): Result<List<DailyIdea>> {
        val dietType = user?.dietCategory ?: "Balanced"
        val budget = if ((user?.budget ?: 0) < 300) "Low" else "Mid"
        val maxTime = user?.maxTime ?: 45

        if (useAi && aiRecipeGenerator is CerebrasRecipeGenerator) {
            Log.d("GenerateAiUseCase", "Attempting AI generation with Cerebras")
            val result = aiRecipeGenerator.generateRecipes(
                mood = mood.displayName,
                dietType = dietType,
                budget = budget,
                maxTime = maxTime,
                ingredients = emptyList(),
                count = 3
            )

            if (result.isSuccess) {
                val aiRecipes = result.getOrThrow()
                val aiIdeas = aiRecipes.map { aiRecipe ->
                    DailyIdea(
                        recipe = aiRecipe.toRecipe(),
                        moodTitle = "✨ AI Magic: ${aiRecipe.title}"
                    )
                }
                
                Log.d("GenerateAiUseCase", "AI Success: Found ${aiIdeas.size} recipes")
                return Result.success(aiIdeas)
            } else {
                Log.e("GenerateAiUseCase", "AI Failed, using fallback", result.exceptionOrNull())
            }
        }

        // Fallback if AI is disabled or fails
        return try {
            val allRecipes = recipeRepository.getAllRecipes()
            val fallbacks = fallbackUseCase(allRecipes, mood, user)
            Log.d("GenerateAiUseCase", "Using fallback: Found ${fallbacks.size} recipes")
            if (fallbacks.isEmpty()) {
                Result.failure(Exception("No recipes found matching your preferences. Please adjust your diet or mood."))
            } else {
                Result.success(fallbacks)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
