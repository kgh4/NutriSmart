package com.example.nutrismart.data.mapper

import com.example.nutrismart.data.local.entity.RecipeEntity
import com.example.nutrismart.domain.model.Recipe

fun RecipeEntity.toDomainModel(): Recipe {
    val budgetStr = when {
        estimatedCost < 5.0 -> "Low"
        estimatedCost < 10.0 -> "Mid"
        else -> "High"
    }
    return Recipe(
        id = id.toString(),
        title = title,
        ingredients = ingredients,
        steps = steps,
        time = prepMinutes,
        calories = estimatedCalories,
        budget = budgetStr,
        dietCategory = dietCategory
    )
}

fun Recipe.toEntity(): RecipeEntity {
    val costVal = when (budget) {
        "Low" -> 2.0
        "Mid" -> 7.0
        "High" -> 15.0
        else -> 5.0
    }
    return RecipeEntity(
        id = id.toIntOrNull() ?: 0,
        title = title,
        description = "",
        ingredients = ingredients,
        steps = steps,
        mealType = "",
        dietType = dietCategory,
        estimatedCost = costVal,
        estimatedCalories = calories,
        prepMinutes = time,
        isFavorite = 0,
        sourceType = "LOCAL",
        dietCategory = dietCategory
    )
}
