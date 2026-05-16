package com.example.nutrismart.domain.model

data class MealSlot(
    val mealType: String = "",
    val recipeId: String? = null,
    val recipe: Recipe? = null
)
