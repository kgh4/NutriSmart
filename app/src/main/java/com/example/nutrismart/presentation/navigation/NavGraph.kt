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
    val appViewModel: AppViewModel = viewModel(factory = ViewModelFactory)

    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route,
        modifier = modifier
    ) {
        composable(Screen.Auth.route) {
            EnhancedAuthScreen(
                appViewModel = appViewModel,
                onLoginSuccess = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Auth.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.Home.route) {
            HomeScreen(
                appViewModel = appViewModel,
                onNavigateToProfile = { navController.navigate(Screen.UserProfile.route) },
                onNavigateToPlanner = { navController.navigate(Screen.MealPlanner.route) },
                onNavigateToDailyIdeas = { navController.navigate(Screen.DailyIdeas.route) },
                onNavigateToLeftovers = { navController.navigate(Screen.LeftoverRemix.route) },
                onNavigateToShoppingList = { navController.navigate(Screen.ShoppingList.route) },
                onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.createRoute(id)) }
            )
        }

        composable(Screen.DailyIdeas.route) {
            val viewModel: DailyIdeasViewModel = viewModel(factory = ViewModelFactory)
            DailyIdeasScreen(
                viewModel = viewModel,
                appViewModel = appViewModel,
                onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.createRoute(id)) },
                onBackClick = { navController.popBackStack() },
                onNavigateToHome = { navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                } },
                onNavigateToPlanner = { navController.navigate(Screen.MealPlanner.route) },
                onNavigateToShopping = { navController.navigate(Screen.ShoppingList.route) },
                onNavigateToProfile = { navController.navigate(Screen.UserProfile.route) }
            )
        }

        composable(Screen.UserProfile.route) {
            val viewModel: ProfileViewModel = viewModel(factory = ViewModelFactory)
            ProfileScreen(
                viewModel = viewModel,
                appViewModel = appViewModel,
                onNavigateToSavedRecipes = { navController.navigate(Screen.SavedRecipes.route) },
                onLogout = {
                    navController.navigate(Screen.Auth.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.SavedRecipes.route) {
            SavedRecipesScreen(
                appViewModel = appViewModel,
                onBackClick = { navController.popBackStack() },
                onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.createRoute(id)) }
            )
        }

        composable(Screen.MealPlanner.route) {
            val viewModel: WeeklyPlannerViewModel = viewModel(factory = ViewModelFactory)
            WeeklyPlannerScreen(
                viewModel = viewModel,
                appViewModel = appViewModel,
                profileId = "default_user",
                onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.LeftoverRemix.route) {
            val viewModel: LeftoverRecipesViewModel = viewModel(factory = ViewModelFactory)
            LeftoverRecipesScreen(
                viewModel = viewModel,
                appViewModel = appViewModel,
                onRecipeClick = { id -> navController.navigate(Screen.RecipeDetails.createRoute(id)) },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.ShoppingList.route) {
            val viewModel: ShoppingListViewModel = viewModel(factory = ViewModelFactory)
            EnhancedShoppingListScreen(
                viewModel = viewModel,
                appViewModel = appViewModel,
                mealPlanId = "active_plan"
            )
        }

        composable(
            route = Screen.RecipeDetails.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId") ?: ""
            val viewModel: RecipeDetailsViewModel = viewModel(factory = ViewModelFactory)
            RecipeDetailsScreen(
                viewModel = viewModel,
                appViewModel = appViewModel,
                recipeId = recipeId,
                onBackClick = { navController.popBackStack() },
                onNavigateToHome = { navController.navigate(Screen.Home.route) {
                    popUpTo(Screen.Home.route) { inclusive = true }
                } },
                onNavigateToPlanner = { navController.navigate(Screen.MealPlanner.route) },
                onNavigateToShopping = { navController.navigate(Screen.ShoppingList.route) },
                onNavigateToProfile = { navController.navigate(Screen.UserProfile.route) }
            )
        }
    }
}
