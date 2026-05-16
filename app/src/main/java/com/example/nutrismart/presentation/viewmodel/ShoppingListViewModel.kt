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
                    it.copy(error = "No plan selected. Please go to Weekly Planner and click 'Use This Plan'.", isLoading = false) 
                }
                return@launch
            }

            try {
                val allRecipes = mutableListOf<Recipe>()
                plan.days.forEach { day ->
                    day.breakfast.recipe?.let { allRecipes.add(it) }
                    day.lunch.recipe?.let { allRecipes.add(it) }
                    day.dinner.recipe?.let { allRecipes.add(it) }
                    day.snack.recipe?.let { allRecipes.add(it) }
                }

                val ingredientLines = allRecipes.flatMap { 
                    it.ingredients.split("\n") 
                }.filter { it.isNotBlank() }

                val counts = ingredientLines.groupingBy { it.trim() }.eachCount()

                val shoppingItems = counts.map { (name, count) ->
                    ShoppingItem(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        quantity = if (count > 1) "$count units" else "1 unit",
                        checked = false,
                        category = detectCategory(name)
                    )
                }

                _uiState.update { it.copy(items = shoppingItems, isLoading = false) }
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message, isLoading = false) }
            }
        }
    }

    fun toggleItem(itemId: String) {
        _uiState.update { state ->
            val updatedItems = state.items.map { item ->
                if (item.id == itemId) item.copy(checked = !item.checked) else item
            }
            state.copy(items = updatedItems)
        }
    }

    fun addItem(name: String) {
        if (name.isBlank()) return
        val newItem = ShoppingItem(
            id = UUID.randomUUID().toString(),
            name = name,
            quantity = "1 unit",
            checked = false,
            category = detectCategory(name)
        )
        _uiState.update { it.copy(items = listOf(newItem) + it.items) }
    }

    fun removeItem(itemId: String) {
        _uiState.update { it.copy(items = it.items.filter { item -> item.id != itemId }) }
    }

    fun updateItems(newItems: List<ShoppingItem>) {
        _uiState.update { it.copy(items = newItems) }
    }

    private fun detectCategory(name: String): String {
        val lower = name.lowercase()
        return when {
            listOf("tomato", "onion", "garlic", "carrot", "broccoli", "spinach", "potato", "pepper", "avocado", "cucumber", "lettuce", "veggie", "vegetable").any { lower.contains(it) } -> "Produce"
            listOf("chicken", "beef", "pork", "fish", "egg", "tofu", "turkey", "salmon", "tuna", "steak").any { lower.contains(it) } -> "Proteins"
            listOf("milk", "cheese", "butter", "yogurt", "cream", "feta").any { lower.contains(it) } -> "Dairy"
            listOf("rice", "pasta", "bread", "flour", "quinoa", "oats", "tortilla", "wrap").any { lower.contains(it) } -> "Grains/Pantry"
            else -> "Other"
        }
    }
}
