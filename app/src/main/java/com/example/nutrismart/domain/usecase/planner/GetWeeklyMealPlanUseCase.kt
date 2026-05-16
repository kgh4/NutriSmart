package com.example.nutrismart.domain.usecase.planner

import com.example.nutrismart.domain.model.WeeklyMealPlan
import com.example.nutrismart.domain.repository.MealPlanRepository

class GetWeeklyMealPlanUseCase(private val repository: MealPlanRepository) {
    suspend operator fun invoke(profileId: String): WeeklyMealPlan? {
        return repository.getWeeklyPlan(profileId)
    }
}
