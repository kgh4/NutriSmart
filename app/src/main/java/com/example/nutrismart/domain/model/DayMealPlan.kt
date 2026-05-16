package com.example.nutrismart.domain.model

data class DayMealPlan(
    val dayOfWeek: String = "",
    val breakfast: MealSlot = MealSlot(),
    val lunch: MealSlot = MealSlot(),
    val dinner: MealSlot = MealSlot(),
    val snack: MealSlot = MealSlot(),
    val dailyCost: Double = 0.0,
    val dailyCalories: Int = 0
)
