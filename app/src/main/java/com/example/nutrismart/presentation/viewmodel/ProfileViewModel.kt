package com.example.nutrismart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.domain.model.UserProfile
import com.example.nutrismart.domain.repository.UserProfileRepository
import com.example.nutrismart.domain.usecase.profile.GetUserProfileUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ProfileUiState(
    val isLoading: Boolean = false,
    val profile: UserProfile? = null,
    val isSaved: Boolean = false,
    val error: String? = null
)

class ProfileViewModel(
    private val getUserProfileUseCase: GetUserProfileUseCase,
    private val repository: UserProfileRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    fun loadProfile() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val profile = getUserProfileUseCase()
                _uiState.update { it.copy(profile = profile, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = e.message ?: "An unexpected error occurred", 
                        isLoading = false 
                    ) 
                }
            }
        }
    }

    fun saveProfile(profile: UserProfile) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isSaved = false) }
            try {
                repository.saveUserProfile(profile)
                _uiState.update { 
                    it.copy(
                        profile = profile, 
                        isSaved = true, 
                        isLoading = false 
                    ) 
                }
            } catch (e: Exception) {
                _uiState.update { 
                    it.copy(
                        error = e.message ?: "An unexpected error occurred", 
                        isLoading = false 
                    ) 
                }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun resetSavedState() {
        _uiState.update { it.copy(isSaved = false) }
    }
}
