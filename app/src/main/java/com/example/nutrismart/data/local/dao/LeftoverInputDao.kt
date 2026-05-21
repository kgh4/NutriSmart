package com.example.nutrismart.data.local.dao

import androidx.room.*
import com.example.nutrismart.data.local.entity.LeftoverInputEntity

@Dao
interface LeftoverInputDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLeftoverInput(input: LeftoverInputEntity): Long

    @Query("SELECT * FROM leftover_inputs WHERE id = :id")
    suspend fun getLeftoverInputById(id: String): LeftoverInputEntity?

    @Delete
    suspend fun deleteLeftoverInput(input: LeftoverInputEntity): Int
}
