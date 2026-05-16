package com.example.nutrismart.domain.repository

import com.example.nutrismart.domain.model.ShoppingList

interface ShoppingListRepository {
    suspend fun getShoppingList(mealPlanId: String): ShoppingList?
    suspend fun saveShoppingList(list: ShoppingList)
    suspend fun deleteShoppingList(list: ShoppingList)
    suspend fun updateShoppingItemChecked(listId: String, itemId: String, checked: Boolean)
}
