package com.example.nutrismart.domain.usecase.estimator

import com.example.nutrismart.domain.model.Recipe

class NutritionEstimator {

    fun estimateDailyCalories(recipes: List<Recipe>): Int {
        return recipes.sumOf { it.calories }
    }

    fun estimateDailyNutrition(recipes: List<Recipe>): Map<String, Double> {
        val totalCalories = estimateDailyCalories(recipes).toDouble()
        // Simple heuristic for macros based on total calories
        // In a real app, these would come from recipe data
        return mapOf(
            "Protein" to totalCalories * 0.25 / 4.0, // 25% protein
            "Carbs" to totalCalories * 0.5 / 4.0,    // 50% carbs
            "Fats" to totalCalories * 0.25 / 9.0     // 25% fats
        )
    }
}
