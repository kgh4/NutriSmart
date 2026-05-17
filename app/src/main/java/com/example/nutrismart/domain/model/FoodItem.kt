package com.example.nutrismart.domain.model

data class FoodItem(
    val id: String,
    val name: String,
    val category: String,
    val pricePerKg: Double,
    val calories: Int,
    val benefits: List<String>,
    val imageUrl: String? = null
)

object FoodDatabase {
    val vegetables = listOf(
        FoodItem(
            id = "tomato",
            name = "Tomato",
            category = "Vegetables",
            pricePerKg = 2.5,
            calories = 18,
            benefits = listOf("Rich in Vitamin C", "Lycopene", "Antioxidants"),
            imageUrl = null
        ),
        FoodItem(
            id = "potato",
            name = "Potato",
            category = "Vegetables",
            pricePerKg = 1.8,
            calories = 77,
            benefits = listOf("Good carbs", "Potassium", "Vitamin B6"),
            imageUrl = null
        ),
        FoodItem(
            id = "onion",
            name = "Onion",
            category = "Vegetables",
            pricePerKg = 1.5,
            calories = 40,
            benefits = listOf("Sulfur compounds", "Anti-inflammatory", "Quercetin"),
            imageUrl = null
        ),
        FoodItem(
            id = "carrot",
            name = "Carrot",
            category = "Vegetables",
            pricePerKg = 2.0,
            calories = 41,
            benefits = listOf("Beta-carotene", "Vitamin A", "Eye health"),
            imageUrl = null
        ),
        FoodItem(
            id = "pepper",
            name = "Bell Pepper",
            category = "Vegetables",
            pricePerKg = 3.5,
            calories = 31,
            benefits = listOf("Vitamin C", "Capsaicin", "Antioxidants"),
            imageUrl = null
        )
    )

    val fruits = listOf(
        FoodItem(
            id = "apple",
            name = "Apple",
            category = "Fruits",
            pricePerKg = 3.0,
            calories = 52,
            benefits = listOf("Fiber", "Vitamin C", "Pectin"),
            imageUrl = null
        ),
        FoodItem(
            id = "banana",
            name = "Banana",
            category = "Fruits",
            pricePerKg = 2.0,
            calories = 89,
            benefits = listOf("Potassium", "Vitamin B6", "Quick energy"),
            imageUrl = null
        ),
        FoodItem(
            id = "orange",
            name = "Orange",
            category = "Fruits",
            pricePerKg = 2.5,
            calories = 47,
            benefits = listOf("Vitamin C", "Citric acid", "Immune boost"),
            imageUrl = null
        )
    )

    fun searchFood(query: String): List<FoodItem> {
        val allFood = vegetables + fruits
        return allFood.filter { it.name.contains(query, ignoreCase = true) }
    }

    fun getFoodByCategory(category: String): List<FoodItem> {
        return (vegetables + fruits).filter { it.category.equals(category, ignoreCase = true) }
    }
}
