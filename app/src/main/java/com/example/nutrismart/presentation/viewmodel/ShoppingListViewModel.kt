package com.example.nutrismart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.domain.model.ShoppingItem
import com.example.nutrismart.domain.repository.DayMealPlanRepository
import com.example.nutrismart.domain.repository.RecipeRepository
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
    private val dayMealPlanRepository: DayMealPlanRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    /**
     * Load shopping list by fetching selected plan and its recipes from DB
     */
    fun loadShoppingList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                // 1. Load active plan (stored with id = 1)
                val plan = dayMealPlanRepository.getMealPlan(1)
                
                if (plan == null) {
                    _uiState.update { 
                        it.copy(error = "No plan selected. Go to Weekly Planner and click 'Use This Day Plan'.", isLoading = false) 
                    }
                    return@launch
                }

                // 2. Resolve recipes from DB
                val recipeIds = listOf(plan.breakfastId, plan.lunchId, plan.dinnerId, plan.snackId)
                    .filter { it.isNotBlank() }
                
                val recipes = recipeIds.mapNotNull { id ->
                    recipeRepository.getRecipeById(id)
                }

                // 3. Extract and group ingredients
                val ingredients = recipes.flatMap { it.ingredients.split("\n") }
                    .filter { it.isNotBlank() }
                
                val grouped = ingredients.groupingBy { it.trim() }.eachCount()

                val shoppingItems = grouped.map { (name, count) ->
                    ShoppingItem(
                        id = UUID.randomUUID().toString(),
                        name = name,
                        quantity = if (count > 1) "$count" else "",
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
            checked = false,
            category = detectCategory(name)
        )
        _uiState.update { it.copy(items = listOf(newItem) + it.items) }
    }

    fun removeItem(itemId: String) {
        _uiState.update { it.copy(items = it.items.filter { it.id != itemId }) }
    }

    private fun detectCategory(name: String): String {
        val lower = name.lowercase()
        return when {
            listOf("tomato", "onion", "garlic", "carrot", "broccoli", "spinach", "potato", "pepper", "avocado", "cucumber", "lettuce", "veggie").any { lower.contains(it) } -> "Produce"
            listOf("chicken", "beef", "pork", "fish", "egg", "tofu", "turkey", "salmon", "tuna").any { lower.contains(it) } -> "Proteins"
            listOf("milk", "cheese", "butter", "yogurt", "cream", "feta").any { lower.contains(it) } -> "Dairy"
            listOf("rice", "pasta", "bread", "flour", "quinoa", "oats", "tortilla").any { lower.contains(it) } -> "Grains/Pantry"
            else -> "Other"
        }
    }
}
