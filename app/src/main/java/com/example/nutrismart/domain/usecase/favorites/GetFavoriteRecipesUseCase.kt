package com.example.nutrismart.domain.usecase.favorites

import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.domain.repository.RecipeRepository

class GetFavoriteRecipesUseCase(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(userId: String): Result<List<Recipe>> {
        return try {
            val favorites = recipeRepository.getFavoriteRecipes(userId)
            Result.success(favorites.sortedBy { it.mealType })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
