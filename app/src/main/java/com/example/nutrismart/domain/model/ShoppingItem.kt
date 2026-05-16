package com.example.nutrismart.domain.model

data class ShoppingItem(
    val id: String = "",
    val name: String = "",
    val quantity: String = "",
    val checked: Boolean = false,
    val category: String = ""
)
