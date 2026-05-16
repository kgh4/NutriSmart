package com.example.nutrismart.domain.model

import java.time.LocalDateTime

data class LeftoverRecipeResult(
    val id: String = "",
    val leftoverInputId: String = "",
    val recipeId: String = "",
    val generatedAt: LocalDateTime = LocalDateTime.now()
)
