package com.example.nutrismart.domain.repository

import com.example.nutrismart.domain.model.WeeklyMealPlan

interface MealPlanRepository {
    suspend fun getWeeklyPlan(profileId: String): WeeklyMealPlan?
    suspend fun getWeeklyPlanById(id: String): WeeklyMealPlan?
    suspend fun saveWeeklyMealPlan(plan: WeeklyMealPlan)
    suspend fun deleteWeeklyPlan(plan: WeeklyMealPlan)
    suspend fun getActiveMealPlan(): WeeklyMealPlan?
}
