package com.example.nutrismart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.domain.model.WeeklyMealPlan
import com.example.nutrismart.domain.repository.MealPlanRepository
import com.example.nutrismart.domain.repository.RecipeRepository
import com.example.nutrismart.domain.usecase.weeklyplanner.GenerateWeeklyMealPlanUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class WeeklyPlannerUiState(
    val isLoading: Boolean = false,
    val mealPlan: WeeklyMealPlan? = null,
    val isPlanActive: Boolean = false,
    val error: String? = null
)

class WeeklyPlannerViewModel(
    private val generateWeeklyMealPlanUseCase: GenerateWeeklyMealPlanUseCase,
    private val recipeRepository: RecipeRepository,
    private val mealPlanRepository: MealPlanRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(WeeklyPlannerUiState())
    val uiState: StateFlow<WeeklyPlannerUiState> = _uiState.asStateFlow()

    private var selectedPlan: WeeklyMealPlan? = null

    fun selectPlan(plan: WeeklyMealPlan) {
        selectedPlan = plan
        _uiState.update { it.copy(isPlanActive = true) }
    }

    fun getSelectedPlan(): WeeklyMealPlan? {
        return selectedPlan
    }

    fun generateWeeklyMealPlan(profileId: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null, isPlanActive = false) }
            val result = generateWeeklyMealPlanUseCase(profileId)
            result.onSuccess { plan ->
                _uiState.update { it.copy(mealPlan = plan, isLoading = false) }
            }.onFailure { exception ->
                _uiState.update { it.copy(error = exception.message ?: "Error", isLoading = false) }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
