package com.example.nutrismart.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.domain.model.UserProfile
import com.example.nutrismart.domain.usecase.profile.SaveUserProfileUseCase
import kotlinx.coroutines.launch

class OnboardingViewModel(
    private val saveUserProfileUseCase: SaveUserProfileUseCase
) : ViewModel() {

    var isLoading by mutableStateOf(false)
        private set

    var isComplete by mutableStateOf(false)
        private set

    var error by mutableStateOf<String?>(null)
        private set

    fun completeOnboarding(
        name: String,
        email: String,
        dietCategory: String,
        cookingSkill: String,
        budget: Double,
        maxTime: Int
    ) {
        viewModelScope.launch {
            isLoading = true
            error = null
            try {
                val profile = UserProfile(
                    id = java.util.UUID.randomUUID().toString(),
                    name = name,
                    dietCategory = dietCategory,
                    cookingSkill = cookingSkill,
                    weeklyBudget = budget,
                    availableMinutesPerDay = maxTime
                )
                saveUserProfileUseCase(profile)
                isComplete = true
            } catch (e: Exception) {
                error = e.message ?: "Failed to save profile"
            } finally {
                isLoading = false
            }
        }
    }
}
