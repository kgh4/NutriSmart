package com.example.nutrismart.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Auth : Screen("auth")
    object RecipeDetails : Screen("recipe_details/{recipeId}") {
        fun createRoute(recipeId: String) = "recipe_details/$recipeId"
    }
    object SavedRecipes : Screen("saved_recipes")
    object Favorites : Screen("favorites")
    object MealPlanner : Screen("meal_planner")
    object ShoppingList : Screen("shopping_list")
    object LeftoverRemix : Screen("leftover_remix")
    object UserProfile : Screen("user_profile")
    object DailyIdeas : Screen("daily_ideas")
}
