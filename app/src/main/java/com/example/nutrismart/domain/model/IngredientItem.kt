package com.example.nutrismart.domain.model

data class IngredientItem(
    val name: String = "",
    val quantity: Double = 0.0,
    val unit: String = "",
    val estimatedPrice: Double = 0.0,
    val isOptional: Boolean = false
)
