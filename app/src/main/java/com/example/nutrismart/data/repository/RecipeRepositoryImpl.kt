package com.example.nutrismart.data.repository

import com.example.nutrismart.data.local.dao.RecipeDao
import com.example.nutrismart.data.mapper.toDomainModel
import com.example.nutrismart.data.mapper.toEntity
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.domain.repository.RecipeRepository

class RecipeRepositoryImpl(
    private val recipeDao: RecipeDao
) : RecipeRepository {

    override suspend fun getAllRecipes(): List<Recipe> {
        return recipeDao.getAllRecipes().map { it.toDomainModel() }
    }

    override suspend fun getRecipeById(id: Int): Recipe? {
        return recipeDao.getRecipeById(id)?.toDomainModel()
    }

    override suspend fun getFavoriteRecipes(): List<Recipe> {
        return recipeDao.getFavoriteRecipes().map { it.toDomainModel() }
    }

    override suspend fun saveRecipe(recipe: Recipe) {
        recipeDao.insertOrUpdate(recipe.toEntity())
    }

    override suspend fun deleteRecipe(recipe: Recipe) {
        recipeDao.deleteRecipe(recipe.toEntity())
    }
}
