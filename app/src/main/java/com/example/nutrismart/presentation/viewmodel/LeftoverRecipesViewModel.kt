package com.example.nutrismart.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.domain.model.User
import com.example.nutrismart.domain.usecase.leftovers.GenerateLeftoverAiRecipesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LeftoverRecipesUiState(
    val isLoading: Boolean = false,
    val recipes: List<Recipe> = emptyList(),
    val error: String? = null,
    val currentInput: String = ""
)

class LeftoverRecipesViewModel(
    private val generateLeftoverAiRecipesUseCase: GenerateLeftoverAiRecipesUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LeftoverRecipesUiState())
    val uiState: StateFlow<LeftoverRecipesUiState> = _uiState.asStateFlow()

    private val _selectedIngredients = mutableStateListOf<String>()
    val selectedIngredients: List<String> = _selectedIngredients

    fun onInputChanged(input: String) {
        _uiState.update { it.copy(currentInput = input) }
    }

    fun addIngredient(ingredient: String) {
        val trimmed = ingredient.trim()
        if (trimmed.isNotBlank() && !_selectedIngredients.contains(trimmed)) {
            _selectedIngredients.add(trimmed)
            _uiState.update { it.copy(currentInput = "") }
        }
    }

    fun removeIngredient(ingredient: String) {
        _selectedIngredients.remove(ingredient)
    }

    fun generateRecipeIdeas(user: User? = null) {
        if (_selectedIngredients.isEmpty()) {
            _uiState.update { it.copy(error = "Please add some ingredients first", recipes = emptyList()) }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val result = generateLeftoverAiRecipesUseCase(_selectedIngredients, user)
            
            if (result.isSuccess) {
                _uiState.update { 
                    it.copy(
                        recipes = result.getOrThrow(),
                        isLoading = false
                    )
                }
            } else {
                _uiState.update {
                    it.copy(
                        error = result.exceptionOrNull()?.message ?: "Failed to generate recipes",
                        isLoading = false
                    )
                }
            }
        }
    }
}
