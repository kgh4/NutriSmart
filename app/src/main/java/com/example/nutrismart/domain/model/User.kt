package com.example.nutrismart.domain.model

data class User(
    val id: String = "",
    val name: String = "",
    val email: String = "",
    val dietCategory: String = "Balanced",
    val budget: Int = 0,
    val maxTime: Int = 0
)

