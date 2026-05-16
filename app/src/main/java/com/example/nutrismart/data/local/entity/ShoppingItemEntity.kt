package com.example.nutrismart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "shopping_items")
data class ShoppingItemEntity(
    @PrimaryKey val id: String,
    val name: String,
    val quantity: Double,
    val unit: String,
    val checked: Boolean,
    val category: String
)
