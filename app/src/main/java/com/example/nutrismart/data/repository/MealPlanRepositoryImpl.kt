package com.example.nutrismart.data.repository

import com.example.nutrismart.data.local.dao.WeeklyMealPlanDao
import com.example.nutrismart.data.mapper.toDomainModel
import com.example.nutrismart.data.mapper.toEntity
import com.example.nutrismart.domain.model.WeeklyMealPlan
import com.example.nutrismart.domain.repository.MealPlanRepository

class MealPlanRepositoryImpl(
    private val mealPlanDao: WeeklyMealPlanDao
) : MealPlanRepository {

    override suspend fun getWeeklyPlan(profileId: String): WeeklyMealPlan? {
        return mealPlanDao.getWeeklyPlan(profileId)?.toDomainModel()
    }

    override suspend fun getWeeklyPlanById(id: String): WeeklyMealPlan? {
        return mealPlanDao.getWeeklyPlanById(id)?.toDomainModel()
    }

    override suspend fun saveWeeklyMealPlan(plan: WeeklyMealPlan) {
        mealPlanDao.saveWeeklyPlan(plan.toEntity())
    }

    override suspend fun deleteWeeklyPlan(plan: WeeklyMealPlan) {
        mealPlanDao.deleteWeeklyPlan(plan.toEntity())
    }

    override suspend fun getActiveMealPlan(): WeeklyMealPlan? {
        return mealPlanDao.getWeeklyPlanById("active_plan")?.toDomainModel()
    }
}
