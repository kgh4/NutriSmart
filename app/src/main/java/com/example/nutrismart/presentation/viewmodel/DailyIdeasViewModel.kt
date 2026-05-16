package com.example.nutrismart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.domain.repository.RecipeRepository
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
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DailyIdeasUiState())
    val uiState: StateFlow<DailyIdeasUiState> = _uiState.asStateFlow()

    private var allRecipes: List<Recipe> = emptyList()

    init {
        loadAllRecipes()
    }

    private fun loadAllRecipes() {
        viewModelScope.launch {
            try {
                allRecipes = recipeRepository.getAllRecipes()
                if (_uiState.value.ideas.isEmpty()) {
                    generateDailyIdeas()
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = "Failed to load recipes") }
            }
        }
    }

    fun generateDailyIdeas() {
        if (allRecipes.isEmpty()) return
        
        _uiState.update { it.copy(isLoading = true) }
        
        // Simple AI Logic: Shuffle and take 5 unique recipes
        val newIdeas = allRecipes
            .shuffled()
            .distinctBy { it.title }
            .take(5)
            
        _uiState.update { 
            it.copy(
                ideas = newIdeas,
                isLoading = false,
                error = null
            )
        }
    }
}
