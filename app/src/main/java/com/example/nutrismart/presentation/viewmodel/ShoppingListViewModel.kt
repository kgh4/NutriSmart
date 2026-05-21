package com.example.nutrismart.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.data.datasource.TunisianProductDataSource
import com.example.nutrismart.domain.model.Product
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
    val error: String? = null,
    val searchResults: List<Product> = emptyList(),
    val selectedProduct: Product? = null
)

class ShoppingListViewModel(
    private val dayMealPlanRepository: DayMealPlanRepository,
    private val recipeRepository: RecipeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    var currentWeight by mutableStateOf(1.0)
        private set

    /**
     * Load shopping list by fetching selected plan and its recipes from DB
     */
    fun loadShoppingList() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                val plan = dayMealPlanRepository.getMealPlan(1)
                
                if (plan == null) {
                    _uiState.update { 
                        it.copy(error = "No plan selected. Go to Weekly Planner and click 'Use This Day Plan'.", isLoading = false) 
                    }
                    return@launch
                }

                val recipeIds = listOf(plan.breakfastId, plan.lunchId, plan.dinnerId, plan.snackId)
                    .filter { it.isNotBlank() }
                
                val recipes = recipeIds.mapNotNull { id ->
                    recipeRepository.getRecipeById(id)
                }

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

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
        if (query.length >= 2) {
            val results = TunisianProductDataSource.products.filter { 
                it.name.contains(query, ignoreCase = true) 
            }
            _uiState.update { it.copy(searchResults = results) }
        } else {
            _uiState.update { it.copy(searchResults = emptyList()) }
        }
    }

    fun selectProduct(product: Product) {
        _uiState.update { it.copy(selectedProduct = product, searchResults = emptyList()) }
        searchQuery = ""
        currentWeight = 1.0
    }

    fun updateWeight(weight: Double) {
        currentWeight = weight
    }

    fun addSelectedProduct() {
        val product = uiState.value.selectedProduct ?: return
        val finalPrice = product.pricePerUnit * currentWeight
        val newItem = ShoppingItem(
            id = UUID.randomUUID().toString(),
            name = product.name,
            quantity = "${String.format("%.1f", currentWeight)}${product.unit}",
            price = finalPrice,
            checked = false,
            category = product.category
        )
        _uiState.update { it.copy(items = listOf(newItem) + it.items, selectedProduct = null) }
    }

    fun toggleItem(itemId: String, onPurchaseConfirmed: (Double) -> Unit = {}) {
        _uiState.update { state ->
            val updatedItems = state.items.map { item ->
                if (item.id == itemId) {
                    val newChecked = !item.checked
                    if (newChecked && item.price > 0) {
                        onPurchaseConfirmed(item.price)
                    }
                    item.copy(checked = newChecked)
                } else item
            }
            state.copy(items = updatedItems)
        }
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
