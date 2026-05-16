package com.example.nutrismart.data.repository

import com.example.nutrismart.data.local.dao.MealPlanDao
import com.example.nutrismart.data.mapper.toDomainModel
import com.example.nutrismart.data.mapper.toEntity
import com.example.nutrismart.domain.model.MealPlan
import com.example.nutrismart.domain.repository.DayMealPlanRepository

class DayMealPlanRepositoryImpl(
    private val mealPlanDao: MealPlanDao
) : DayMealPlanRepository {

    override suspend fun getMealPlan(id: Int): MealPlan? {
        return mealPlanDao.getMealPlan(id)?.toDomainModel()
    }

    override suspend fun getAllMealPlans(): List<MealPlan> {
        return mealPlanDao.getAllMealPlans().map { it.toDomainModel() }
    }

    override suspend fun saveMealPlan(mealPlan: MealPlan) {
        mealPlanDao.insert(mealPlan.toEntity())
    }

    override suspend fun deleteMealPlan(id: Int) {
        mealPlanDao.deleteById(id)
    }
}

