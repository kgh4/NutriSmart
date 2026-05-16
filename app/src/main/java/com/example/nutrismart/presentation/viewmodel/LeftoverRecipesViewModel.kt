package com.example.nutrismart.presentation.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.example.nutrismart.domain.generator.DietRecipeProvider
import com.example.nutrismart.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class LeftoverRecipesUiState(
    val isLoading: Boolean = false,
    val recipes: List<Recipe> = emptyList(),
    val error: String? = null,
    val currentInput: String = ""
)

class LeftoverRecipesViewModel(
    private val dietRecipeProvider: DietRecipeProvider = DietRecipeProvider()
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

    fun generateRecipeIdeas() {
        if (_selectedIngredients.isEmpty()) {
            _uiState.update { it.copy(error = "Please add some ingredients first", recipes = emptyList()) }
            return
        }

        _uiState.update { it.copy(isLoading = true, error = null) }
        
        try {
            // Get all recipes to filter locally
            val allRecipes = dietRecipeProvider.getRecipes("All")
            
            val filtered = allRecipes.filter { recipe ->
                _selectedIngredients.any { ingredient ->
                    recipe.ingredients.lowercase().contains(ingredient.lowercase())
                }
            }

            if (filtered.isEmpty()) {
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
                        recipes = filtered,
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
