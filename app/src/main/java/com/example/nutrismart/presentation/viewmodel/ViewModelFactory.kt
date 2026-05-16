package com.example.nutrismart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.nutrismart.NutriSmartApplication

object ViewModelFactory : ViewModelProvider.Factory {
    private var weeklyPlannerViewModel: WeeklyPlannerViewModel? = null

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as NutriSmartApplication
        val container = application.container

        return when {
            modelClass.isAssignableFrom(ProfileViewModel::class.java) -> {
                ProfileViewModel(container.getUserProfileUseCase, container.userProfileRepository) as T
            }
            modelClass.isAssignableFrom(WeeklyPlannerViewModel::class.java) -> {
                if (weeklyPlannerViewModel == null) {
                    weeklyPlannerViewModel = WeeklyPlannerViewModel(
                        container.generateWeeklyMealPlanUseCase,
                        container.recipeRepository,
                        container.mealPlanRepository
                    )
                }
                weeklyPlannerViewModel as T
            }
            modelClass.isAssignableFrom(DailyIdeasViewModel::class.java) -> {
                DailyIdeasViewModel(container.generateDailyMealIdeasUseCase) as T
            }
            modelClass.isAssignableFrom(LeftoverRecipesViewModel::class.java) -> {
                LeftoverRecipesViewModel(container.leftoverRecipeGenerator) as T
            }
            modelClass.isAssignableFrom(ShoppingListViewModel::class.java) -> {
                val planner = weeklyPlannerViewModel ?: throw IllegalStateException("Planner must be initialized first")
                ShoppingListViewModel(planner) as T
            }
            modelClass.isAssignableFrom(RecipeDetailsViewModel::class.java) -> {
                RecipeDetailsViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
