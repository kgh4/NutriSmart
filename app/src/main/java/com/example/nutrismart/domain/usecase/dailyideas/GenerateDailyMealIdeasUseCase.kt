package com.example.nutrismart.domain.usecase.dailyideas

import com.example.nutrismart.domain.generator.DietRecipeProvider
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.domain.repository.RecipeRepository
import com.example.nutrismart.domain.repository.UserProfileRepository

class GenerateDailyMealIdeasUseCase(
    private val recipeRepository: RecipeRepository,
    private val userProfileRepository: UserProfileRepository,
    private val dietRecipeProvider: DietRecipeProvider = DietRecipeProvider()
) {
    /**
     * Generates a set of up to 6 meal ideas based on the user profile constraints.
     * Prioritizes variety across different meal types.
     */
    suspend operator fun invoke(profileId: String): Result<List<Recipe>> {
        return try {
            val profile = userProfileRepository.getUserProfile()
            val dietCategory = profile?.dietCategory ?: "Balanced"
            
            val filtered = dietRecipeProvider.getRecipes(dietCategory)

            Result.success(filtered)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
