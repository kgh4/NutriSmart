package com.example.nutrismart.domain.model

import java.time.LocalDateTime

data class LeftoverInput(
    val id: String = "",
    val rawText: String = "",
    val createdAt: LocalDateTime = LocalDateTime.now()
)
