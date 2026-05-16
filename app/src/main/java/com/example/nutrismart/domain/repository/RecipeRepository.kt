package com.example.nutrismart.domain.repository

import com.example.nutrismart.domain.model.Recipe

interface RecipeRepository {
    suspend fun getAllRecipes(): List<Recipe>
    suspend fun getRecipeById(id: Int): Recipe?
    suspend fun getFavoriteRecipes(): List<Recipe>
    suspend fun saveRecipe(recipe: Recipe)
    suspend fun deleteRecipe(recipe: Recipe)
}
