package com.example.nutrismart.data.repository

import com.example.nutrismart.data.local.dao.ShoppingListDao
import com.example.nutrismart.data.mapper.toDomainModel
import com.example.nutrismart.data.mapper.toEntity
import com.example.nutrismart.domain.model.ShoppingList
import com.example.nutrismart.domain.repository.ShoppingListRepository

class ShoppingListRepositoryImpl(
    private val shoppingListDao: ShoppingListDao
) : ShoppingListRepository {

    override suspend fun getShoppingList(mealPlanId: String): ShoppingList? {
        return shoppingListDao.getShoppingList(mealPlanId)?.toDomainModel()
    }

    override suspend fun saveShoppingList(list: ShoppingList) {
        shoppingListDao.saveShoppingList(list.toEntity())
    }

    override suspend fun deleteShoppingList(list: ShoppingList) {
        shoppingListDao.deleteShoppingList(list.toEntity())
    }

    override suspend fun updateShoppingItemChecked(listId: String, itemId: String, checked: Boolean) {
        val listEntity = shoppingListDao.getShoppingListById(listId) ?: return
        val domainList = listEntity.toDomainModel()
        val updatedItems = domainList.items.map {
            if (it.id == itemId) it.copy(checked = checked) else it
        }
        val updatedList = domainList.copy(items = updatedItems)
        shoppingListDao.saveShoppingList(updatedList.toEntity())
    }
}
