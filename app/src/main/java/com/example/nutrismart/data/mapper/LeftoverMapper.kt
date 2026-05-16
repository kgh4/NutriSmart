package com.example.nutrismart.data.mapper

import com.example.nutrismart.data.local.entity.LeftoverInputEntity
import com.example.nutrismart.data.local.entity.LeftoverRecipeResultEntity
import com.example.nutrismart.domain.model.LeftoverInput
import com.example.nutrismart.domain.model.LeftoverRecipeResult
import java.time.LocalDateTime

fun LeftoverInputEntity.toDomainModel(): LeftoverInput {
    return LeftoverInput(
        id = id,
        rawText = rawText,
        createdAt = LocalDateTime.parse(createdAt)
    )
}

fun LeftoverInput.toEntity(): LeftoverInputEntity {
    return LeftoverInputEntity(
        id = id,
        rawText = rawText,
        createdAt = createdAt.toString()
    )
}

fun LeftoverRecipeResultEntity.toDomainModel(): LeftoverRecipeResult {
    return LeftoverRecipeResult(
        id = id,
        leftoverInputId = leftoverInputId,
        recipeId = recipeId,
        generatedAt = LocalDateTime.parse(generatedAt)
    )
}

fun LeftoverRecipeResult.toEntity(): LeftoverRecipeResultEntity {
    return LeftoverRecipeResultEntity(
        id = id,
        leftoverInputId = leftoverInputId,
        recipeId = recipeId,
        generatedAt = generatedAt.toString()
    )
}
