package com.example.nutrismart.domain.repository

import com.example.nutrismart.domain.model.Favorite

interface FavoriteRepository {
    suspend fun getFavorite(recipeId: String): Favorite?
    suspend fun getAllFavorites(): List<Favorite>
    suspend fun saveFavorite(favorite: Favorite)
    suspend fun deleteFavorite(recipeId: String)
}

