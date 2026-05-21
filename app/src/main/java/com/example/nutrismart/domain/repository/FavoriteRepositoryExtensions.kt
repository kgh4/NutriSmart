package com.example.nutrismart.domain.repository

import com.example.nutrismart.domain.model.Favorite
import com.example.nutrismart.domain.model.Recipe

/**
 * Extension function to check and interact with favorites
 */
suspend fun FavoriteRepository.isFavorite(recipeId: String): Boolean {
    return getFavorite(recipeId) != null
}

/**
 * Extension function to toggle favorite status
 */
suspend fun FavoriteRepository.toggleFavorite(recipe: Recipe) {
    if (isFavorite(recipe.id)) {
        deleteFavorite(recipe.id)
    } else {
        saveFavorite(Favorite(recipeId = recipe.id))
    }
}

/**
 * Get favorite recipe IDs only
 */
suspend fun FavoriteRepository.getFavoriteIds(): Set<String> {
    return getAllFavorites().map { it.recipeId }.toSet()
}

