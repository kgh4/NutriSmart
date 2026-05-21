package com.example.nutrismart.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.example.nutrismart.NutriSmartApplication

object ViewModelFactory : ViewModelProvider.Factory {
    private var weeklyPlannerViewModel: WeeklyPlannerViewModel? = null
    private var appViewModel: AppViewModel? = null
    private var onboardingViewModel: OnboardingViewModel? = null

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as NutriSmartApplication
        val container = application.container

        return when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> {
                if (appViewModel == null) {
                    appViewModel = AppViewModel(
                        userRepository = container.userRepository,
                        recipeRepository = container.recipeRepository,
                        favoriteRepository = container.favoriteRepository,
                        dayMealPlanRepository = container.dayMealPlanRepository
                    )
                }
                appViewModel as T
            }
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
            modelClass.isAssignableFrom(OnboardingViewModel::class.java) -> {
                if (onboardingViewModel == null) {
                    onboardingViewModel = OnboardingViewModel(container.saveUserProfileUseCase)
                }
                onboardingViewModel as T
            }
            modelClass.isAssignableFrom(DailyIdeasViewModel::class.java) -> {
                DailyIdeasViewModel(container.recipeRepository, container.generateAiDailyIdeasUseCase) as T
            }
            modelClass.isAssignableFrom(LeftoverRecipesViewModel::class.java) -> {
                LeftoverRecipesViewModel() as T
            }
            modelClass.isAssignableFrom(ShoppingListViewModel::class.java) -> {
                ShoppingListViewModel(container.dayMealPlanRepository, container.recipeRepository) as T
            }
            modelClass.isAssignableFrom(RecipeDetailsViewModel::class.java) -> {
                RecipeDetailsViewModel() as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}
