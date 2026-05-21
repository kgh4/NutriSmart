package com.example.nutrismart.data.mapper

import com.example.nutrismart.data.local.entity.RecipeEntity
import com.example.nutrismart.domain.model.Recipe

fun RecipeEntity.toDomainModel(): Recipe {
    return Recipe(
        id = id,
        title = title,
        description = description,
        ingredients = ingredients,
        steps = steps,
        mealType = mealType,
        time = time,
        calories = calories,
        budget = budget,
        dietCategory = dietCategory
    )
}

fun Recipe.toEntity(): RecipeEntity {
    return RecipeEntity(
        id = id,
        title = title,
        description = description,
        ingredients = ingredients,
        steps = steps,
        mealType = mealType,
        calories = calories,
        time = time,
        budget = budget,
        dietCategory = dietCategory
    )
}
