package com.example.nutrismart.domain.generator

import com.example.nutrismart.domain.model.Recipe

class LeftoverRecipeGenerator(
    private val dietRecipeProvider: DietRecipeProvider = DietRecipeProvider()
) {
    /**
     * Finds recipes that match the provided leftover ingredients.
     * Match logic: returns all recipes where ANY input ingredient is found.
     */
    fun generateRecipes(userInput: String): List<Recipe> {
        val input = userInput.lowercase().trim()
        if (input.isEmpty()) return emptyList()

        // Split input by space to support multiple ingredients
        val inputIngredients = input.split(" ").filter { it.isNotBlank() }
        
        // Get all available recipes from the provider
        val allRecipes = dietRecipeProvider.getRecipes("Balanced") // Returns all flattened recipes if category not found

        return allRecipes.filter { recipe ->
            val recipeIngredients = recipe.ingredients.lowercase()
            inputIngredients.any { ingredient ->
                recipeIngredients.contains(ingredient)
            }
        }
    }
}
