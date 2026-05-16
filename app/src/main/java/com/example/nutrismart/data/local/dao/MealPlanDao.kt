package com.example.nutrismart.data.local.dao

import androidx.room.*
import com.example.nutrismart.data.local.entity.MealPlanEntity

@Dao
interface MealPlanDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mealPlan: MealPlanEntity): Long

    @Query("SELECT * FROM meal_plans WHERE id = :id")
    suspend fun getMealPlan(id: Int): MealPlanEntity?

    @Query("SELECT * FROM meal_plans")
    suspend fun getAllMealPlans(): List<MealPlanEntity>

    @Delete
    suspend fun delete(mealPlan: MealPlanEntity): Int

    @Query("DELETE FROM meal_plans WHERE id = :id")
    suspend fun deleteById(id: Int): Int
}

