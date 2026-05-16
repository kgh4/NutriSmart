package com.example.nutrismart.domain.model

import java.time.LocalDateTime

data class ShoppingList(
    val id: String = "",
    val mealPlanId: String = "",
    val items: List<ShoppingItem> = emptyList(),
    val createdAt: LocalDateTime = LocalDateTime.now()
)
