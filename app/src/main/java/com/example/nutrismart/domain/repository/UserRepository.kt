package com.example.nutrismart.domain.repository

import com.example.nutrismart.domain.model.User

interface UserRepository {
    suspend fun getUser(id: String): User?
    suspend fun getUserByEmail(email: String): User?
    suspend fun getUserProfile(): User?
    suspend fun saveUser(user: User)
    suspend fun deleteUser(id: String)
}

