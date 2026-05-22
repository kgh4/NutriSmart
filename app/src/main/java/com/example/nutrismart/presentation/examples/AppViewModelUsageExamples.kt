package com.example.nutrismart.presentation.examples

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.presentation.viewmodel.AppViewModel
import kotlinx.coroutines.launch

/**
 * Example: Recipe Card with Favorite Button
 * Shows how to use AppViewModel to toggle favorites
 */
@Composable
fun RecipeCardExample(
    recipe: Recipe,
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val favoriteIds = viewModel.favoriteRecipeIds.collectAsState()
    val isFavorite = recipe.id in favoriteIds.value

    Column(modifier = modifier.padding(16.dp)) {
        Text(text = recipe.title)
        Text(text = "Time: ${recipe.time} min | Calories: ${recipe.calories}")

        IconButton(
            onClick = { viewModel.toggleFavorite(recipe) }
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                contentDescription = "Toggle favorite"
            )
        }
    }
}

/**
 * Example: Load Recipes Screen
 * Shows loading state and displaying recipes
 */
@Composable
fun RecipesScreenExample(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val allRecipes = viewModel.allRecipes.collectAsState()
    val currentUser = viewModel.currentUser.collectAsState()
    val isLoading = viewModel.isLoading
    val scope = rememberCoroutineScope()

    Box(modifier = modifier.fillMaxSize()) {
        if (isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        } else {
            Column(modifier = Modifier.fillMaxSize()) {
                // User info
                currentUser.value?.let { user ->
                    Text(text = "Welcome, ${user.name}")
                    Text(text = "Diet: ${user.dietCategory}")
                }

                // Load recipes button
                Button(onClick = { scope.launch { viewModel.loadRecipes() } }) {
                    Text("Load Recipes")
                }

                // Display recipes
                allRecipes.value.forEach { recipe ->
                    RecipeCardExample(recipe = recipe, viewModel = viewModel)
                }
            }
        }
    }
}

/**
 * Example: Daily Ideas Screen
 * Shows how to use AI engine through ViewModel
 */
@Composable
fun DailyIdeasScreenExample(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val dailyIdeas = viewModel.dailyIdeas.collectAsState()
    val currentUser = viewModel.currentUser.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Daily Recipe Ideas")

        // Generate ideas based on user constraints
        Button(
            onClick = {
                currentUser.value?.let { user ->
                    viewModel.generateDailyIdeas(
                        dietCategory = user.dietCategory,
                        maxTime = user.maxTime,
                        budget = when {
                            user.budget < 200 -> "Low"
                            user.budget < 500 -> "Mid"
                            else -> "High"
                        }
                    )
                }
            }
        ) {
            Text("Generate Ideas")
        }

        // Display daily ideas
        dailyIdeas.value.forEach { recipe ->
            RecipeCardExample(recipe = recipe, viewModel = viewModel)
        }
    }
}

/**
 * Example: Leftover Recipe Finder
 * Shows how to use leftover matching AI
 */
@Composable
fun LeftoverFinderExample(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val ingredients = listOf("eggs", "cheese", "butter", "flour")
    val matchedRecipes = viewModel.findRecipesByLeftovers(ingredients)

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Recipes matching your leftovers: ${ingredients.joinToString(", ")}")

        matchedRecipes.forEach { recipe ->
            RecipeCardExample(recipe = recipe, viewModel = viewModel)
        }
    }
}

/**
 * Example: Favorites Screen
 * Shows how to load and manage favorites
 */
@Composable
fun FavoritesScreenExample(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val savedRecipes = viewModel.savedRecipes
    val favoriteIds = viewModel.favoriteRecipeIds.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("My Favorite Recipes (${favoriteIds.value.size})")

        // Favorite recipes
        savedRecipes.forEach { recipe ->
            RecipeCardExample(recipe = recipe, viewModel = viewModel)
        }

        if (savedRecipes.isEmpty()) {
            Text("No favorite recipes yet")
        }
    }
}

/**
 * Example: Weekly Meal Planning
 * Shows how to use AI to generate weekly plans
 */
@Composable
fun WeeklyPlannerExample(
    viewModel: AppViewModel,
    modifier: Modifier = Modifier
) {
    val currentUser = viewModel.currentUser.collectAsState()

    Column(modifier = modifier.fillMaxSize().padding(16.dp)) {
        Text("Weekly Meal Planner")

        Button(
            onClick = {
                currentUser.value?.let { user ->
                    val weeklyRecipes = viewModel.generateWeeklyPlan(
                        dietCategory = user.dietCategory,
                        maxTime = user.maxTime,
                        budget = when {
                            user.budget < 200 -> "Low"
                            user.budget < 500 -> "Mid"
                            else -> "High"
                        }
                    )
                    // Use weeklyRecipes to display or save meal plans
                }
            }
        ) {
            Text("Generate Weekly Plan")
        }
    }
}

