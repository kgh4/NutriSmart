package com.example.nutrismart.data.repository

import com.example.nutrismart.data.local.dao.FavoriteDao
import com.example.nutrismart.data.mapper.toDomainModel
import com.example.nutrismart.data.mapper.toEntity
import com.example.nutrismart.domain.model.Favorite
import com.example.nutrismart.domain.repository.FavoriteRepository

class FavoriteRepositoryImpl(
    private val favoriteDao: FavoriteDao
) : FavoriteRepository {

    override suspend fun getFavorite(recipeId: String, userId: String): Favorite? {
        return favoriteDao.getFavorite(recipeId, userId)?.toDomainModel()
    }

    override suspend fun getAllFavorites(userId: String): List<Favorite> {
        return favoriteDao.getAllFavorites(userId).map { it.toDomainModel() }
    }

    override suspend fun saveFavorite(favorite: Favorite) {
        favoriteDao.insert(favorite.toEntity())
    }

    override suspend fun deleteFavorite(recipeId: String, userId: String) {
        favoriteDao.deleteById(recipeId, userId)
    }
}

