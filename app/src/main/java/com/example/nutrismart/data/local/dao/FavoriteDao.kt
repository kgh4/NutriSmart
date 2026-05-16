package com.example.nutrismart.data.local.dao

import androidx.room.*
import com.example.nutrismart.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity): Long

    @Query("SELECT * FROM favorites WHERE recipeId = :recipeId")
    suspend fun getFavorite(recipeId: String): FavoriteEntity?

    @Query("SELECT * FROM favorites")
    suspend fun getAllFavorites(): List<FavoriteEntity>

    @Delete
    suspend fun delete(favorite: FavoriteEntity): Int

    @Query("DELETE FROM favorites WHERE recipeId = :recipeId")
    suspend fun deleteById(recipeId: String): Int
}

