package com.example.nutrismart.domain.model

import java.time.LocalDate

data class WeeklyMealPlan(
    val id: String = "",
    val profileId: String = "",
    val weekStartDate: LocalDate = LocalDate.now(),
    val days: List<DayMealPlan> = emptyList(),
    val totalCost: Double = 0.0
)
