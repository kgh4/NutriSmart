package com.example.nutrismart.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.nutrismart.domain.model.DayMealPlan
import com.example.nutrismart.domain.model.Recipe

class AppViewModel : ViewModel() {

    var userName by mutableStateOf("Student")
    var userEmail by mutableStateOf("alex.chen@university.edu")

    val savedRecipes = mutableStateListOf<Recipe>()

    var selectedDayPlan by mutableStateOf<DayMealPlan?>(null)

    fun toggleFavorite(recipe: Recipe) {
        val existing = savedRecipes.find { it.id == recipe.id }
        if (existing != null) {
            savedRecipes.remove(existing)
        } else {
            savedRecipes.add(recipe)
        }
    }

    fun isFavorite(recipeId: String): Boolean {
        return savedRecipes.any { it.id == recipeId }
    }
}
