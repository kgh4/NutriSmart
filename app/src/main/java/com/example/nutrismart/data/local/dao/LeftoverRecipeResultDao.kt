package com.example.nutrismart.data.local.dao

import androidx.room.*
import com.example.nutrismart.data.local.entity.LeftoverRecipeResultEntity

@Dao
interface LeftoverRecipeResultDao {
    @Query("SELECT * FROM leftover_recipe_results WHERE leftoverInputId = :inputId")
    suspend fun getResultsForInput(inputId: String): List<LeftoverRecipeResultEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: LeftoverRecipeResultEntity): Long

    @Delete
    suspend fun deleteResult(result: LeftoverRecipeResultEntity): Int
}
