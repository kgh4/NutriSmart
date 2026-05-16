package com.example.nutrismart.domain.usecase.favorites

import com.example.nutrismart.domain.repository.RecipeRepository

class ToggleFavoriteRecipeUseCase(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipeId: String): Result<Unit> {
        return try {
            val idInt = recipeId.toIntOrNull() ?: return Result.failure(Exception("Invalid ID"))
            val recipe = recipeRepository.getRecipeById(idInt)
            if (recipe != null) {
                recipeRepository.saveRecipe(recipe.copy(isFavorite = !recipe.isFavorite))
                Result.success(Unit)
            } else {
                Result.failure(Exception("Recipe not found"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
