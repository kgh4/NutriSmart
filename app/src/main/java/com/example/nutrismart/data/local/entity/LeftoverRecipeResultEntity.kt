package com.example.nutrismart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leftover_recipe_results")
data class LeftoverRecipeResultEntity(
    @PrimaryKey val id: String,
    val leftoverInputId: String,
    val recipeId: String,
    val generatedAt: String
)
