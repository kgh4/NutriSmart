package com.example.nutrismart.data.local.dao

import androidx.room.*
import com.example.nutrismart.data.local.entity.WeeklyMealPlanEntity

@Dao
interface WeeklyMealPlanDao {
    @Query("SELECT * FROM weekly_meal_plans WHERE profileId = :profileId LIMIT 1")
    suspend fun getWeeklyPlan(profileId: String): WeeklyMealPlanEntity?

    @Query("SELECT * FROM weekly_meal_plans WHERE id = :id")
    suspend fun getWeeklyPlanById(id: String): WeeklyMealPlanEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveWeeklyPlan(plan: WeeklyMealPlanEntity): Long

    @Delete
    suspend fun deleteWeeklyPlan(plan: WeeklyMealPlanEntity): Int
}
