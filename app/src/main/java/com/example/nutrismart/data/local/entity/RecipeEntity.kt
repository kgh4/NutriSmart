package com.example.nutrismart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String,
    val ingredients: String,
    val steps: String,
    val mealType: String,
    val dietType: String,
    val estimatedCost: Double,
    val estimatedCalories: Int,
    val prepMinutes: Int,
    val isFavorite: Int,
    val sourceType: String,
    val dietCategory: String
)
