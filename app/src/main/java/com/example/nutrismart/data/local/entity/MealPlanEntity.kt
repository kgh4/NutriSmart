package com.example.nutrismart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "meal_plans")
data class MealPlanEntity(
    @PrimaryKey
    val id: Int,
    val day: String,
    val breakfastId: String,
    val lunchId: String,
    val dinnerId: String,
    val snackId: String
)

