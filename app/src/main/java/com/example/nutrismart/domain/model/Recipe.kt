package com.example.nutrismart.domain.model

data class Recipe(
    val id: String = "",
    val title: String = "",
    val description: String = "",
    val ingredients: String = "",
    val steps: String = "",
    val mealType: String = "",
    val time: Int = 0,
    val calories: Int = 0,
    val budget: String = "Low",
    val dietCategory: String = "Balanced",
    val isFavorite: Boolean = false
)
