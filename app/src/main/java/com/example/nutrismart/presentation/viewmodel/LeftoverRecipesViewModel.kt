package com.example.nutrismart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.domain.generator.LeftoverRecipeGenerator
import com.example.nutrismart.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LeftoverRecipesUiState(
    val isLoading: Boolean = false,
    val recipes: List<Recipe> = emptyList(),
    val error: String? = null,
    val searchQuery: String = ""
)

class LeftoverRecipesViewModel(
    private val leftoverRecipeGenerator: LeftoverRecipeGenerator
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeftoverRecipesUiState())
    val uiState: StateFlow<LeftoverRecipesUiState> = _uiState.asStateFlow()

    fun onQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun searchRecipes() {
        val query = _uiState.value.searchQuery
        if (query.isBlank()) {
            _uiState.update { it.copy(recipes = emptyList(), error = null) }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }
        
        try {
            val results = leftoverRecipeGenerator.generateRecipes(query)
            if (results.isEmpty()) {
                _uiState.update { 
                    it.copy(
                        recipes = emptyList(), 
                        isLoading = false,
                        error = "No recipes found, try different ingredients"
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        recipes = results,
                        isLoading = false
                    )
                }
            }
        } catch (e: Exception) {
            _uiState.update {
                it.copy(
                    error = e.message ?: "Failed to find recipes",
                    isLoading = false
                )
            }
        }
    }
}
