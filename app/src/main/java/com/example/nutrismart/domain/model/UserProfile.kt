package com.example.nutrismart.domain.model

data class UserProfile(
    val id: String = "",
    val name: String = "",
    val dietType: String = "",
    val dietCategory: String = "Balanced",
    val weeklyBudget: Double = 0.0,
    val cookingSkill: String = "",
    val availableMinutesPerDay: Int = 0,
    val scheduleType: String = "",
    val allergies: List<String> = emptyList(),
    val dislikedFoods: List<String> = emptyList()
)
