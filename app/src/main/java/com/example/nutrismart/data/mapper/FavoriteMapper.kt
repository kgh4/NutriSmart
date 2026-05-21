package com.example.nutrismart.data.mapper

import com.example.nutrismart.data.local.entity.FavoriteEntity
import com.example.nutrismart.domain.model.Favorite

fun FavoriteEntity.toDomainModel(): Favorite {
    return Favorite(
        recipeId = recipeId
    )
}

fun Favorite.toEntity(): FavoriteEntity {
    return FavoriteEntity(
        recipeId = recipeId
    )
}
