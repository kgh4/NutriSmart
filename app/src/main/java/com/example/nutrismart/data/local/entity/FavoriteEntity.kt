package com.example.nutrismart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorites", primaryKeys = ["recipeId", "userId"])
data class FavoriteEntity(
    val recipeId: String,
    val userId: String = ""
)

