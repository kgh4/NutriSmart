package com.example.nutrismart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "weekly_meal_plans")
data class WeeklyMealPlanEntity(
    @PrimaryKey val id: String,
    val userId: String,
    val weekStartDate: String,
    val daysJson: String,
    val totalCost: Double
)
