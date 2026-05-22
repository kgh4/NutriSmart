package com.example.nutrismart.domain.usecase.leftovers

import android.util.Log
import com.example.nutrismart.domain.ai.AIEngine
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.domain.model.User
import com.example.nutrismart.domain.repository.RecipeRepository
import com.example.nutrismart.domain.service.AiRecipeGenerator
import com.example.nutrismart.domain.service.toRecipe
import com.example.nutrismart.data.ai.CerebrasRecipeGenerator

class GenerateLeftoverAiRecipesUseCase(
    private val aiRecipeGenerator: AiRecipeGenerator,
    private val recipeRepository: RecipeRepository,
    private val aiEngine: AIEngine
) {
    suspend operator fun invoke(
        ingredients: List<String>,
        user: User?
    ): Result<List<Recipe>> {
        val generator = aiRecipeGenerator as? CerebrasRecipeGenerator
        
        if (generator != null) {
            val dietType = user?.dietCategory ?: "Balanced"
            val budget = if ((user?.budget ?: 0) < 300) "Low" else "Mid"
            val maxTime = user?.maxTime ?: 45

            val result = generator.generateRecipes(
                mood = "Creative",
                dietType = dietType,
                budget = budget,
                maxTime = maxTime,
                ingredients = ingredients,
                count = 3
            )

            if (result.isSuccess) {
                return result.map { list -> list.map { it.toRecipe() } }
            } else {
                Log.e("GenerateLeftoverUseCase", "AI Failed, falling back to local DB matches", result.exceptionOrNull())
            }
        }

        return try {
            val allRecipes = recipeRepository.getAllRecipes()
            val fallbacks = aiEngine.findRecipesByLeftovers(ingredients, allRecipes)
            
            if (fallbacks.isEmpty()) {
                Result.failure(Exception("No leftover recipes found matching your ingredients."))
            } else {
                Log.d("GenerateLeftoverUseCase", "Using fallback: Found ${fallbacks.size} matching recipes")
                Result.success(fallbacks)
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
