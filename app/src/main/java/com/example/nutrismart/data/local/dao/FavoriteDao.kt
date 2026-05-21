package com.example.nutrismart.data.local.dao

import androidx.room.*
import com.example.nutrismart.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(favorite: FavoriteEntity): Long

    @Query("SELECT * FROM favorites WHERE recipeId = :recipeId AND userId = :userId")
    suspend fun getFavorite(recipeId: String, userId: String): FavoriteEntity?

    @Query("SELECT * FROM favorites WHERE userId = :userId")
    suspend fun getAllFavorites(userId: String): List<FavoriteEntity>

    @Delete
    suspend fun delete(favorite: FavoriteEntity): Int

    @Query("DELETE FROM favorites WHERE recipeId = :recipeId AND userId = :userId")
    suspend fun deleteById(recipeId: String, userId: String): Int
}

