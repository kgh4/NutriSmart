package com.example.nutrismart.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "leftover_inputs")
data class LeftoverInputEntity(
    @PrimaryKey val id: String,
    val rawText: String,
    val createdAt: String
)
