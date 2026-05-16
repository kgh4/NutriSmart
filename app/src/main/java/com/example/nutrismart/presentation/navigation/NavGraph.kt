package com.example.nutrismart.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.nutrismart.presentation.home.HomeScreen
import com.example.nutrismart.presentation.screens.*
import com.example.nutrismart.presentation.viewmodel.*
import androidx.navigation.NavType
import androidx.navigation.navArgument

@Composable
fun NutriSmartNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
        modifier = modifier
    ) {
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToProfile = { navController.navigate(Screen.UserProfile.route) },
                onNavigateToPlanner = { navController.navigate(Screen.MealPlanner.route) },
                onNavigateToDailyIdeas = { navController.navigate(Screen.DailyIdeas.route) },
                onNavigateToLeftovers = { navController.navigate(Screen.LeftoverRemix.route) },
                onNavigateToShoppingList = { navController.navigate(Screen.ShoppingList.route) }
            )
        }

        composable(Screen.DailyIdeas.route) {
            val viewModel: DailyIdeasViewModel = viewModel(factory = ViewModelFactory)
            DailyIdeasScreen(
                viewModel = viewModel,
                profileId = "default_user",
                onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.createRoute(id)) }
            )
        }

        composable(Screen.UserProfile.route) {
            val viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory)
            ProfileScreen(viewModel = viewModel)
        }

        composable(Screen.MealPlanner.route) {
            val viewModel: WeeklyPlannerViewModel = viewModel(factory = ViewModelFactory)
            WeeklyPlannerScreen(
                viewModel = viewModel,
                profileId = "default_user",
                onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.createRoute(id)) }
            )
        }

        composable(Screen.LeftoverRemix.route) {
            val viewModel: LeftoverRecipesViewModel = viewModel(factory = ViewModelFactory)
            LeftoverRecipesScreen(
                viewModel = viewModel,
                onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.createRoute(id)) }
            )
        }

        composable(Screen.ShoppingList.route) {
            val viewModel: ShoppingListViewModel = viewModel(factory = ViewModelFactory)
            ShoppingListScreen(viewModel = viewModel, mealPlanId = "active_plan")
        }

        composable(
            route = Screen.RecipeDetails.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            val viewModel: RecipeDetailsViewModel = viewModel(factory = ViewModelFactory)
            RecipeDetailsScreen(
                viewModel = viewModel,
                recipeId = recipeId,
                onBackClick = { navController.popBackStack() }
            )
        }
    }
}
