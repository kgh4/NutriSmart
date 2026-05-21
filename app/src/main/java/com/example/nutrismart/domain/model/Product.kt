package com.example.nutrismart.domain.model

data class Product(
    val id: String,
    val name: String,
    val pricePerUnit: Double, // Price in TND per unit (usually per kg)
    val unit: String = "kg",
    val calories: Int, // calories per 100g/unit
    val proteins: Double, // proteins per 100g/unit
    val carbs: Double,
    val fats: Double,
    val category: String,
    val imageRes: Int? = null
)
