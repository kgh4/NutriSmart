package com.example.nutrismart.data.local.dao

import androidx.room.*
import com.example.nutrismart.data.local.entity.ShoppingListEntity

@Dao
interface ShoppingListDao {
    @Query("SELECT * FROM shopping_lists WHERE mealPlanId = :mealPlanId LIMIT 1")
    suspend fun getShoppingList(mealPlanId: String): ShoppingListEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveShoppingList(list: ShoppingListEntity): Long

    @Delete
    suspend fun deleteShoppingList(list: ShoppingListEntity): Int

    @Query("SELECT * FROM shopping_lists WHERE id = :id")
    suspend fun getShoppingListById(id: String): ShoppingListEntity?
}
