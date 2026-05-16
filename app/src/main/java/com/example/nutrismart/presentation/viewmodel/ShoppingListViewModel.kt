package com.example.nutrismart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.domain.model.ShoppingItem
import com.example.nutrismart.domain.model.Recipe
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class ShoppingListUiState(
    val isLoading: Boolean = false,
    val items: List<ShoppingItem> = emptyList(),
    val error: String? = null
)

class ShoppingListViewModel(
    private val weeklyPlannerViewModel: WeeklyPlannerViewModel
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    fun loadShoppingList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            val plan = weeklyPlannerViewModel.getSelectedPlan()
            
            if (plan == null) {
                _uiState.update { 
                    it.copy(error = "No plan selected", isLoading = false, items = emptyList()) 
                }
                return@launch
            }

            val allRecipes = mutableListOf<Recipe>()
            plan.days.forEach { day ->
                day.breakfast.recipe?.let { allRecipes.add(it) }
                day.lunch.recipe?.let { allRecipes.add(it) }
                day.dinner.recipe?.let { allRecipes.add(it) }
                day.snack.recipe?.let { allRecipes.add(it) }
            }

            val ingredients = allRecipes.flatMap { it.ingredients.split("\n") }
            
            val grouped = ingredients.filter { it.isNotBlank() }
                .groupingBy { it.trim() }
                .eachCount()

            val shoppingItems = grouped.map { (name, count) ->
                ShoppingItem(
                    id = UUID.randomUUID().toString(),
                    name = "$name ($count units)",
                    checked = false
                )
            }

            _uiState.update { it.copy(items = shoppingItems, isLoading = false) }
        }
    }

    fun toggleItem(itemId: String, checked: Boolean) {
        _uiState.update { state ->
            val updatedItems = state.items.map { item ->
                if (item.id == itemId) item.copy(checked = checked) else item
            }
            state.copy(items = updatedItems)
        }
    }
}
