package com.example.nutrismart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.nutrismart.domain.generator.DietRecipeProvider
import com.example.nutrismart.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class RecipeDetailsUiState(
    val recipe: Recipe? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

class RecipeDetailsViewModel(
    private val dietRecipeProvider: DietRecipeProvider = DietRecipeProvider()
) : ViewModel() {

    private val _uiState = MutableStateFlow(RecipeDetailsUiState())
    val uiState: StateFlow<RecipeDetailsUiState> = _uiState.asStateFlow()

    fun loadRecipe(recipeId: String) {
        _uiState.update { it.copy(isLoading = true) }
        val recipe = dietRecipeProvider.getRecipeById(recipeId)
        if (recipe != null) {
            _uiState.update { it.copy(recipe = recipe, isLoading = false) }
        } else {
            _uiState.update { it.copy(error = "Recipe not found", isLoading = false) }
        }
    }
}
