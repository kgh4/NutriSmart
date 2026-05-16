package com.example.nutrismart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.domain.usecase.dailyideas.GenerateDailyMealIdeasUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DailyIdeasUiState(
    val isLoading: Boolean = false,
    val ideas: List<Recipe> = emptyList(),
    val error: String? = null
)

class DailyIdeasViewModel(
    private val generateDailyMealIdeasUseCase: GenerateDailyMealIdeasUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyIdeasUiState())
    val uiState: StateFlow<DailyIdeasUiState> = _uiState.asStateFlow()

    fun loadIdeas(profileId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = generateDailyMealIdeasUseCase(profileId)
            
            result.onSuccess { ideas ->
                _uiState.update { 
                    it.copy(
                        ideas = ideas, 
                        isLoading = false 
                    ) 
                }
            }.onFailure { exception ->
                _uiState.update { 
                    it.copy(
                        error = exception.message ?: "Failed to load meal ideas", 
                        isLoading = false 
                    ) 
                }
            }
        }
    }
}
