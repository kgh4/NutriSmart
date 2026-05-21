package com.example.nutrismart.data.local.dao

import androidx.room.*
import com.example.nutrismart.data.local.entity.RecipeEntity

@Dao
interface RecipeDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(recipe: RecipeEntity): Long

    @Query("SELECT * FROM recipes WHERE id = :id")
    suspend fun getRecipe(id: String): RecipeEntity?

    @Query("SELECT * FROM recipes")
    suspend fun getAllRecipes(): List<RecipeEntity>

    @Query("SELECT recipes.* FROM recipes INNER JOIN favorites ON recipes.id = favorites.recipeId")
    suspend fun getFavoriteRecipes(): List<RecipeEntity>

    @Delete
    suspend fun delete(recipe: RecipeEntity): Int

    @Query("DELETE FROM recipes WHERE id = :id")
    suspend fun deleteById(id: String): Int
}
