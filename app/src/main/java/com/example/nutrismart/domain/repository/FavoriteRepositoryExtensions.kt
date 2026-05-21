package com.example.nutrismart.domain.repository

import com.example.nutrismart.domain.model.Favorite
import com.example.nutrismart.domain.model.Recipe

/**
 * Extension function to check and interact with favorites
 */
suspend fun FavoriteRepository.isFavorite(recipeId: String, userId: String): Boolean {
    return getFavorite(recipeId, userId) != null
}

/**
 * Extension function to toggle favorite status
 */
suspend fun FavoriteRepository.toggleFavorite(recipe: Recipe, userId: String) {
    if (isFavorite(recipe.id, userId)) {
        deleteFavorite(recipe.id, userId)
    } else {
        saveFavorite(Favorite(recipeId = recipe.id, userId = userId))
    }
}

/**
 * Get favorite recipe IDs only
 */
suspend fun FavoriteRepository.getFavoriteIds(userId: String): Set<String> {
    return getAllFavorites(userId).map { it.recipeId }.toSet()
}
