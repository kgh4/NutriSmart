package com.example.nutrismart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_profiles")
data class UserProfileEntity(
    @PrimaryKey val id: String,
    val name: String,
    val dietType: String,
    val dietCategory: String,
    val weeklyBudget: Double,
    val cookingSkill: String,
    val availableMinutesPerDay: Int,
    val scheduleType: String,
    val allergies: String, // Stored as JSON string
    val dislikedFoods: String // Stored as JSON string
)
