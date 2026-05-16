package com.example.nutrismart.domain.model

data class ShoppingItem(
    val id: String = "",
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val checked: Boolean = false,
    val category: String = "",
    val price: Double = 0.0,
    val totalCost: Double = 0.0
)
