package com.example.nutrismart.data.repository

import com.example.nutrismart.data.local.dao.UserProfileDao
import com.example.nutrismart.data.mapper.toDomainModel
import com.example.nutrismart.data.mapper.toEntity
import com.example.nutrismart.domain.model.UserProfile
import com.example.nutrismart.domain.repository.UserProfileRepository

class UserProfileRepositoryImpl(
    private val userProfileDao: UserProfileDao
) : UserProfileRepository {

    override suspend fun getUserProfile(): UserProfile? {
        return userProfileDao.getUserProfile()?.toDomainModel()
    }

    override suspend fun saveUserProfile(profile: UserProfile) {
        userProfileDao.insertOrUpdate(profile.toEntity())
    }
}
