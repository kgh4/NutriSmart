package com.example.nutrismart.presentation.navigation

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.nutrismart.presentation.home.HomeScreen
import com.example.nutrismart.presentation.screens.*
import com.example.nutrismart.presentation.viewmodel.*

private const val TAG = "SafeNavGraph"

/**
 * SAFE Navigation Graph
 *
 * Best Practices:
 * 1. Remove try-catch around composable invocations (illegal in Compose).
 * 2. Handle data/initialization errors in ViewModels.
 * 3. Use null-safe argument handling.
 */
@Composable
fun SafeNutriSmartNavGraph(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    // AppViewModel initialization
    val appViewModel: AppViewModel = viewModel(factory = ViewModelFactory)

    NavHost(
        navController = navController,
        startDestination = Screen.Auth.route,
        modifier = modifier
    ) {
        composable(Screen.Auth.route) {
            AuthScreen(
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
                onNavigateToProfile = {
                    navController.navigate(Screen.UserProfile.route)
                },
                onNavigateToPlanner = {
                    navController.navigate(Screen.MealPlanner.route)
                },
                onNavigateToDailyIdeas = {
                    navController.navigate(Screen.DailyIdeas.route)
                },
                onNavigateToLeftovers = {
                    navController.navigate(Screen.LeftoverRemix.route)
                },
                onNavigateToShoppingList = {
                    navController.navigate(Screen.ShoppingList.route)
                },
                onRecipeClick = { id ->
                    if (id.isNotBlank()) {
                        navController.navigate(Screen.RecipeDetails.createRoute(id))
                    }
                }
            )
        }

        composable(Screen.DailyIdeas.route) {
            val dailyIdeasViewModel: DailyIdeasViewModel = viewModel(factory = ViewModelFactory)

            DailyIdeasScreen(
                viewModel = dailyIdeasViewModel,
                onRecipeClick = { id ->
                    if (id.isNotBlank()) {
                        navController.navigate(Screen.RecipeDetails.createRoute(id))
                    }
                },
                onBackClick = { navController.popBackStack() },
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToPlanner = {
                    navController.navigate(Screen.MealPlanner.route)
                },
                onNavigateToShopping = {
                    navController.navigate(Screen.ShoppingList.route)
                },
                onNavigateToProfile = {
                    navController.navigate(Screen.UserProfile.route)
                }
            )
        }

        composable(Screen.UserProfile.route) {
            val profileViewModel: ProfileViewModel = viewModel(factory = ViewModelFactory)

            ProfileScreen(
                viewModel = profileViewModel,
                appViewModel = appViewModel,
                onNavigateToSavedRecipes = {
                    navController.navigate(Screen.SavedRecipes.route)
                },
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
                onRecipeClick = { id ->
                    if (id.isNotBlank()) {
                        navController.navigate(Screen.RecipeDetails.createRoute(id))
                    }
                }
            )
        }

        composable(Screen.MealPlanner.route) {
            val plannerViewModel: WeeklyPlannerViewModel = viewModel(factory = ViewModelFactory)

            WeeklyPlannerScreen(
                viewModel = plannerViewModel,
                appViewModel = appViewModel,
                profileId = "default_user",
                onRecipeClick = { id ->
                    if (id.isNotBlank()) {
                        navController.navigate(Screen.RecipeDetails.createRoute(id))
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.LeftoverRemix.route) {
            val leftoverViewModel: LeftoverRecipesViewModel = viewModel(factory = ViewModelFactory)

            LeftoverRecipesScreen(
                viewModel = leftoverViewModel,
                onRecipeClick = { id ->
                    if (id.isNotBlank()) {
                        navController.navigate(Screen.RecipeDetails.createRoute(id))
                    }
                },
                onBackClick = { navController.popBackStack() }
            )
        }

        composable(Screen.ShoppingList.route) {
            val shoppingViewModel: ShoppingListViewModel = viewModel(factory = ViewModelFactory)

            // Business logic for redirection should ideally be in a ViewModel or use a side effect
            // For now, simple check within the composable is okay if followed by a UI state
            val selectedPlan = appViewModel.selectedDayPlan
            if (selectedPlan == null) {
                ErrorFallbackScreen(
                    message = "Please select a meal plan first",
                    onBack = { navController.popBackStack() }
                )
            } else {
                ShoppingListScreen(
                    viewModel = shoppingViewModel,
                    appViewModel = appViewModel,
                    mealPlanId = "active_plan"
                )
            }
        }

        composable(
            route = Screen.RecipeDetails.route,
            arguments = listOf(navArgument("recipeId") { type = NavType.StringType })
        ) { backStackEntry ->
            val recipeId = backStackEntry.arguments?.getString("recipeId")?.takeIf { it.isNotBlank() }

            if (recipeId == null) {
                ErrorFallbackScreen(
                    message = "Recipe ID is missing",
                    onBack = { navController.popBackStack() }
                )
            } else {
                val detailsViewModel: RecipeDetailsViewModel = viewModel(factory = ViewModelFactory)
                RecipeDetailsScreen(
                    viewModel = detailsViewModel,
                    appViewModel = appViewModel,
                    recipeId = recipeId,
                    onBackClick = { navController.popBackStack() },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(Screen.Home.route) { inclusive = true }
                        }
                    },
                    onNavigateToPlanner = {
                        navController.navigate(Screen.MealPlanner.route)
                    },
                    onNavigateToShopping = {
                        navController.navigate(Screen.ShoppingList.route)
                    },
                    onNavigateToProfile = {
                        navController.navigate(Screen.UserProfile.route)
                    }
                )
            }
        }
    }
}

/**
 * Fallback error screen
 */
@Composable
private fun ErrorFallbackScreen(message: String, onBack: () -> Unit) {
    Scaffold(
        containerColor = Color(0xFFF8F9FA)
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Error,
                    contentDescription = "Error",
                    tint = Color.Red,
                    modifier = Modifier.size(64.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Something went wrong",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1A1C1E)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = message,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(24.dp))
                Button(onClick = onBack) {
                    Text("Go Back")
                }
            }
        }
    }
}
