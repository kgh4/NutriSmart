package com.example.nutrismart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_lists")
data class ShoppingListEntity(
    @PrimaryKey val id: String,
    val userId: String = "",
    val mealPlanId: String,
    val itemsJson: String,
    val createdAt: String
)
