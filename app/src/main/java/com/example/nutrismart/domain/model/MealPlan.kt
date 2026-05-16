package com.example.nutrismart.domain.model

data class MealPlan(
    val id: Int = 0,
    val day: String = "",
    val breakfastId: String = "",
    val lunchId: String = "",
    val dinnerId: String = "",
    val snackId: String = ""
)

