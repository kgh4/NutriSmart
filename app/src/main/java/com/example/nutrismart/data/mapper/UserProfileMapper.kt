package com.example.nutrismart.data.mapper

import com.example.nutrismart.data.local.entity.UserProfileEntity
import com.example.nutrismart.domain.model.UserProfile

fun UserProfileEntity.toDomainModel(): UserProfile {
    return UserProfile(
        id = id,
        name = name,
        dietType = dietType,
        dietCategory = dietCategory,
        weeklyBudget = weeklyBudget,
        cookingSkill = cookingSkill,
        availableMinutesPerDay = availableMinutesPerDay,
        scheduleType = scheduleType,
        allergies = if (allergies.isEmpty()) emptyList() else allergies.split(","),
        dislikedFoods = if (dislikedFoods.isEmpty()) emptyList() else dislikedFoods.split(",")
    )
}

fun UserProfile.toEntity(): UserProfileEntity {
    return UserProfileEntity(
        id = id,
        name = name,
        dietType = dietType,
        dietCategory = dietCategory,
        weeklyBudget = weeklyBudget,
        cookingSkill = cookingSkill,
        availableMinutesPerDay = availableMinutesPerDay,
        scheduleType = scheduleType,
        allergies = allergies.joinToString(","),
        dislikedFoods = dislikedFoods.joinToString(",")
    )
}
