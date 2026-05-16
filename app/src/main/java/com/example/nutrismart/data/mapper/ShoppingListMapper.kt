package com.example.nutrismart.data.mapper

import com.example.nutrismart.data.local.entity.ShoppingListEntity
import com.example.nutrismart.domain.model.ShoppingItem
import com.example.nutrismart.domain.model.ShoppingList
import java.time.LocalDateTime

fun ShoppingListEntity.toDomainModel(): ShoppingList {
    return ShoppingList(
        id = id,
        mealPlanId = mealPlanId,
        createdAt = LocalDateTime.parse(createdAt),
        items = if (itemsJson.isEmpty()) emptyList() else itemsJson.split(";").map { itemStr ->
            val parts = itemStr.split("|")
            ShoppingItem(
                id = parts[0],
                name = parts[1],
                quantity = parts[2].toDouble(),
                unit = parts[3],
                checked = parts[4] == "1",
                category = parts[5]
            )
        }
    )
}

fun ShoppingList.toEntity(): ShoppingListEntity {
    val itemsJson = items.joinToString(";") { item ->
        "${item.id}|${item.name}|${item.quantity}|${item.unit}|${if (item.checked) "1" else "0"}|${item.category}"
    }
    return ShoppingListEntity(
        id = id,
        mealPlanId = mealPlanId,
        itemsJson = itemsJson,
        createdAt = createdAt.toString()
    )
}
