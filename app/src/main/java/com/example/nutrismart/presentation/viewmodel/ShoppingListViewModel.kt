package com.example.nutrismart.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.data.datasource.TunisianProductDataSource
import com.example.nutrismart.domain.model.Product
import com.example.nutrismart.domain.model.ShoppingItem
import com.example.nutrismart.domain.repository.ShoppingListRepository
import com.example.nutrismart.domain.usecase.shoppinglist.GenerateShoppingListUseCase
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
    val selectedProduct: Product? = null,
    val listId: String = ""
)

class ShoppingListViewModel(
    private val shoppingListRepository: ShoppingListRepository,
    private val generateShoppingListUseCase: GenerateShoppingListUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ShoppingListUiState())
    val uiState: StateFlow<ShoppingListUiState> = _uiState.asStateFlow()

    var searchQuery by mutableStateOf("")
        private set

    var currentWeight by mutableStateOf(1.0)
        private set

    /**
     * Load shopping list from repository or generate a new one from the active plan
     */
    fun loadShoppingList(mealPlanId: String = "active_plan") {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            
            try {
                // Try to get existing list
                var list = shoppingListRepository.getShoppingList(mealPlanId)
                
                // If no list exists, generate one from the plan
                if (list == null) {
                    val result = generateShoppingListUseCase(mealPlanId)
                    if (result.isSuccess) {
                        list = result.getOrNull()
                    } else {
                        _uiState.update { 
                            it.copy(error = result.exceptionOrNull()?.message ?: "Error", isLoading = false) 
                        }
                        return@launch
                    }
                }

                _uiState.update { 
                    it.copy(
                        items = list?.items ?: emptyList(), 
                        listId = list?.id ?: "",
                        isLoading = false 
                    ) 
                }
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
        
        val updatedItems = listOf(newItem) + uiState.value.items
        _uiState.update { it.copy(items = updatedItems, selectedProduct = null) }
        saveList()
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
        saveList()
    }

    fun removeItem(itemId: String) {
        _uiState.update { it.copy(items = it.items.filter { it.id != itemId }) }
        saveList()
    }

    private fun saveList() {
        viewModelScope.launch {
            val state = uiState.value
            val list = com.example.nutrismart.domain.model.ShoppingList(
                id = state.listId.ifBlank { UUID.randomUUID().toString() },
                mealPlanId = "active_plan",
                items = state.items,
                createdAt = java.time.LocalDateTime.now()
            )
            shoppingListRepository.saveShoppingList(list)
        }
    }
}

