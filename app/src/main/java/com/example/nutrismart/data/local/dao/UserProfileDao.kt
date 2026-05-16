package com.example.nutrismart.data.local.dao

import androidx.room.*
import com.example.nutrismart.data.local.entity.UserProfileEntity

@Dao
interface UserProfileDao {
    @Query("SELECT * FROM user_profiles LIMIT 1")
    suspend fun getUserProfile(): UserProfileEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(profile: UserProfileEntity): Long
}
