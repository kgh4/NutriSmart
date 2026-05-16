package com.example.nutrismart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "recipes")
data class RecipeEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val ingredients: String,
    val steps: String,
    val calories: Int,
    val time: Int,
    val budget: String,
    val dietCategory: String
)
