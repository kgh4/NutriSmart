package com.example.nutrismart.data.mapper

import com.example.nutrismart.data.local.entity.MealPlanEntity
import com.example.nutrismart.domain.model.MealPlan

fun MealPlanEntity.toDomainModel(): MealPlan {
    return MealPlan(
        id = id,
        day = day,
        breakfastId = breakfastId,
        lunchId = lunchId,
        dinnerId = dinnerId,
        snackId = snackId
    )
}

fun MealPlan.toEntity(): MealPlanEntity {
    return MealPlanEntity(
        id = id,
        day = day,
        breakfastId = breakfastId,
        lunchId = lunchId,
        dinnerId = dinnerId,
        snackId = snackId
    )
}

