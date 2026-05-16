package com.example.nutrismart.domain.generator

class IngredientPriceProvider {
    fun getPrice(ingredient: String): Int {
        val key = ingredient.lowercase().trim()
        return when {
            key.contains("egg") -> 200
            key.contains("bread") -> 230
            key.contains("milk") -> 1300
            key.contains("rice") -> 2800
            key.contains("pasta") -> 800
            key.contains("tomato") -> 1000
            key.contains("tuna") -> 5000
            key.contains("chicken") -> 7000
            key.contains("cheese") -> 1200
            key.contains("vegetable") -> 1500
            else -> 1000
        }
    }
}
