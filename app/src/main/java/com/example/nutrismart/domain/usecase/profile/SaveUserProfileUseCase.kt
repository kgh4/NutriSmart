package com.example.nutrismart.domain.usecase.profile

import com.example.nutrismart.domain.model.UserProfile
import com.example.nutrismart.domain.repository.UserProfileRepository

class SaveUserProfileUseCase(private val repository: UserProfileRepository) {
    suspend operator fun invoke(profile: UserProfile) {
        repository.saveUserProfile(profile)
    }
}
