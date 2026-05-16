package com.example.nutrismart.domain.repository

import com.example.nutrismart.domain.model.UserProfile

interface UserProfileRepository {
    suspend fun getUserProfile(): UserProfile?
    suspend fun saveUserProfile(profile: UserProfile)
}
