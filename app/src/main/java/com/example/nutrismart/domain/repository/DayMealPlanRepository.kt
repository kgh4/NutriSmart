package com.example.nutrismart.domain.repository

import com.example.nutrismart.domain.model.MealPlan

interface DayMealPlanRepository {
    suspend fun getMealPlan(id: Int): MealPlan?
    suspend fun getAllMealPlans(): List<MealPlan>
    suspend fun saveMealPlan(mealPlan: MealPlan)
    suspend fun deleteMealPlan(id: Int)
}

