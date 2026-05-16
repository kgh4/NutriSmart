package com.example.nutrismart.domain.usecase.estimator

import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.domain.model.WeeklyMealPlan

class CostEstimator {

    fun estimateMealCost(recipe: Recipe): Double {
        return when (recipe.budget) {
            "Low" -> 2.0
            "Mid" -> 7.0
            "High" -> 15.0
            else -> 5.0
        }
    }

    fun estimateDailyCost(recipes: List<Recipe>): Double {
        return recipes.sumOf { estimateMealCost(it) }
    }

    fun estimateWeeklyCost(weeklyMealPlan: WeeklyMealPlan): Double {
        return weeklyMealPlan.days.sumOf { it.dailyCost }
    }
}
