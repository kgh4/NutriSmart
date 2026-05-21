package com.example.nutrismart.data.local.dao

import androidx.room.*
import com.example.nutrismart.data.local.entity.UserEntity

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(user: UserEntity): Long

    @Query("SELECT * FROM users WHERE id = :id")
    suspend fun getUser(id: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity?

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun signIn(email: String, password: String): UserEntity?

    @Query("SELECT * FROM users LIMIT 1")
    suspend fun getUserProfile(): UserEntity?

    @Delete
    suspend fun delete(user: UserEntity): Int

    @Query("DELETE FROM users WHERE id = :id")
    suspend fun deleteById(id: String): Int
}

