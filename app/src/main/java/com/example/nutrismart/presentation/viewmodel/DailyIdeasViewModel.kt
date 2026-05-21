package com.example.nutrismart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.domain.model.DailyIdea
import com.example.nutrismart.domain.model.MoodType
import com.example.nutrismart.domain.model.User
import com.example.nutrismart.domain.repository.RecipeRepository
import com.example.nutrismart.domain.usecase.dailyideas.GenerateAiDailyIdeasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DailyIdeasUiState(
    val isLoading: Boolean = false,
    val ideas: List<DailyIdea> = emptyList(),
    val error: String? = null
)

class DailyIdeasViewModel(
    private val recipeRepository: RecipeRepository,
    private val generateAiDailyIdeasUseCase: GenerateAiDailyIdeasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyIdeasUiState())
    val uiState: StateFlow<DailyIdeasUiState> = _uiState.asStateFlow()

    fun generateDailyIdeas(mood: MoodType, user: User?) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            try {
                val moodIdeas = generateAiDailyIdeasUseCase(mood, user)
                _uiState.update { it.copy(ideas = moodIdeas, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }
}
