package com.example.nutrismart.presentation.examples

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.presentation.viewmodel.AppViewModel
import com.example.nutrismart.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private const val TAG = "SafeUsageExamples"

/**
 * SAFE CODE EXAMPLES FOR NUTRISMART
 *
 * This file demonstrates the correct way to handle null safety
 * and prevent crashes in the NutriSmart application.
 */

// ============================================================================
// EXAMPLE 1: Safe ViewModel State Access
// ============================================================================

@Composable
fun SafeUserDisplay(appViewModel: AppViewModel) {
    // ❌ WRONG - Can crash:
    // Text(text = appViewModel.currentUser.value!!.name)

    // ✅ CORRECT - Safe:
    val currentUser = appViewModel.currentUser.value

    if (currentUser != null) {
        Text(text = "Welcome, ${currentUser.name}")
    } else {
        Text(text = "Please sign in")
    }
}

// ============================================================================
// EXAMPLE 2: Safe List Access
// ============================================================================

@Composable
fun SafeRecipeList(recipes: List<Recipe>) {
    // ❌ WRONG - Crashes on empty list:
    // val firstRecipe = recipes[0]
    // val lastRecipe = recipes.last()

    // ✅ CORRECT - Safe methods:
    val firstRecipe = recipes.firstOrNull()
    val lastRecipe = recipes.lastOrNull()

    if (recipes.isEmpty()) {
        Text("No recipes available")
    } else {
        Text("First recipe: ${firstRecipe?.title ?: "Unknown"}")
        Text("Last recipe: ${lastRecipe?.title ?: "Unknown"}")
    }
}

// ============================================================================
// EXAMPLE 3: Safe Navigation Arguments
// ============================================================================

/**
 * WRONG - Crashes if argument is null or missing:
 *
 * composable(Screen.RecipeDetails.route) { backStackEntry ->
 *     val recipeId = backStackEntry.arguments?.getString("recipeId")!!
 *     RecipeDetailsScreen(recipeId = recipeId)
 * }
 */

/**
 * CORRECT - Handles missing arguments gracefully:
 *
 * composable(Screen.RecipeDetails.route) { backStackEntry ->
 *     val recipeId = backStackEntry.arguments?.getString("recipeId")
 *
 *     if (recipeId?.isNotBlank() == true) {
 *         RecipeDetailsScreen(recipeId = recipeId)
 *     } else {
 *         ErrorScreen("Recipe not found")
 *     }
 * }
 */

// ============================================================================
// EXAMPLE 4: Safe Error Handling in Coroutines
// ============================================================================

fun SafeLoadData(appViewModel: AppViewModel, onError: (String) -> Unit) {
    // ✅ CORRECT - Launch a coroutine and handle errors safely
    CoroutineScope(Dispatchers.Main).launch {
        try {
            withContext(Dispatchers.IO) {
                appViewModel.loadRecipes()
            }
            Log.d(TAG, "Recipes loaded successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Failed to load recipes: ${e.message}", e)
            onError("Failed to load recipes")
        }
    }
}

// ============================================================================
// EXAMPLE 5: Safe String Operations
// ============================================================================

@Composable
fun SafeStringDisplay(userName: String?, userEmail: String?) {
    // ❌ WRONG - Can show "null" or crash:
    // Text(userName) // Shows "null" if null
    // Text(userName!!) // Crashes if null

    // ✅ CORRECT - Safe defaults:
    val safeName = userName?.takeIf { it.isNotBlank() } ?: "Guest"
    val safeEmail = userEmail?.takeIf { it.isNotBlank() } ?: "not@provided.com"

    Text("Name: $safeName")
    Text("Email: $safeEmail")
}

// ============================================================================
// EXAMPLE 6: Safe Collections Operations
// ============================================================================

fun SafeCollectionOperations(recipes: List<Recipe>?) {
    // ❌ WRONG - Multiple crash points:
    // val count = recipes!!.size
    // val first = recipes[0]
    // recipes.forEach { it.title.length } // title could be null

    // ✅ CORRECT - Fully safe:
    val count = recipes?.size ?: 0
    val first = recipes?.firstOrNull()
    recipes?.forEach { recipe ->
        val titleLength = recipe.title?.length ?: 0
        Log.d(TAG, "Recipe title length: $titleLength")
    }
}

// ============================================================================
// EXAMPLE 7: Safe Conditional Logic
// ============================================================================

@Composable
fun SafeConditionalUI(appViewModel: AppViewModel) {
    // ❌ WRONG - Unsafe assumption:
    // if (appViewModel.currentUser.value != null) {
    //     val user = appViewModel.currentUser.value // Could be null here!
    //     Text(user!!.name)
    // }

    // ✅ CORRECT - Properly safe:
    val currentUser = appViewModel.currentUser.value
    if (currentUser != null) {
        Text("User: ${currentUser.name}")
    } else {
        Text("No user logged in")
    }
}

// ============================================================================
// EXAMPLE 8: Safe Meal Plan Access
// ============================================================================

@Composable
fun SafeMealPlanAccess(appViewModel: AppViewModel) {
    // ❌ WRONG - selectedDayPlan could become null:
    // val meals = appViewModel.selectedDayPlan!!.meals

    // ✅ CORRECT - Safe access with fallback:
    val selectedPlan = appViewModel.selectedDayPlan

    if (selectedPlan != null) {
        // Safe to use selectedPlan here
        Text("Meal plan for ${selectedPlan.dayOfWeek}")
    } else {
        Box(modifier = Modifier.padding(16.dp)) {
            Text("Please select a meal plan first", color = Color.Gray)
        }
    }
}

// ============================================================================
// EXAMPLE 9: Safe Favorite Toggle
// ============================================================================

fun SafeFavoriteToggle(appViewModel: AppViewModel, recipe: Recipe) {
    // ❌ WRONG - Recipe could be null:
    // appViewModel.toggleFavorite(recipe!!)

    // ✅ CORRECT - Validate before using:
    if (recipe.id.isNotBlank()) {
        appViewModel.toggleFavorite(recipe)
        Log.d(TAG, "Toggled favorite for: ${recipe.title}")
    } else {
        Log.w(TAG, "Cannot toggle favorite for recipe with empty ID")
    }
}

// ============================================================================
// EXAMPLE 10: Safe Data Transformation
// ============================================================================

fun SafeDataTransformation(recipes: List<Recipe>?) {
    // ❌ WRONG - Can crash or produce unexpected results:
    // val titles = recipes!!.map { it.title }

    // ✅ CORRECT - Safe transformation:
    val titles = recipes
        ?.mapNotNull { recipe -> recipe.title?.takeIf { it.isNotBlank() } }
        ?: emptyList()

    Log.d(TAG, "Found ${titles.size} recipes with valid titles")
}

// ============================================================================
// EXAMPLE 11: Using SafetyUtils for Common Operations
// ============================================================================

@Composable
fun SafetyUtilsExamples(recipes: List<Recipe>?) {
    // Safe first element
    val firstRecipe = SafetyUtils.safeFirst(recipes)

    // Safe element by index
    val thirdRecipe = SafetyUtils.safeGet(recipes, 2)

    // Safe string operations
    val title = "  ".orEmpty() // Returns ""

    // Safe list operations
    val safeList = SafetyUtils.orEmptyList(recipes)

    // Safe int conversion
    val calories = "500".safeToInt(0) // Returns 500
    val invalid = "abc".safeToInt(100) // Returns 100 (default)

    // Safe operations with try-catch wrapper
    val result = SafetyUtils.safeCall(
        block = { recipes?.size ?: 0 },
        default = 0,
        onError = { error -> Log.e(TAG, "Error: $error") }
    )
}

// ============================================================================
// EXAMPLE 12: Complete Safe Screen Implementation
// ============================================================================

@Composable
fun CompleteSafeScreenExample(appViewModel: AppViewModel) {
    val currentUser = appViewModel.currentUser.value

    // Safety check before using data
    val greeting = if (currentUser != null && currentUser.name.isNotBlank()) {
        "Hi ${currentUser.name}!"
    } else {
        "Welcome to NutriSmart"
    }

    Text(text = greeting)

    // Safe list access
    val recipes = appViewModel.allRecipes.value
    if (recipes.isEmpty()) {
        Text("No recipes available yet")
    } else {
        // Safe to use recipes here
        recipes.forEach { recipe ->
            val safeTitle = recipe.title?.takeIf { it.isNotBlank() } ?: "Untitled"
            val safeTime = recipe.time?.toString() ?: "N/A"
            Text("$safeTitle ($safeTime min)")
        }
    }

    // Safe error message display
    val errorMsg = appViewModel.errorMessage
    if (errorMsg.isNotBlank()) {
        Text(text = "Error: $errorMsg", color = Color.Red)
    }
}

// ============================================================================
// EXAMPLE 13: Safe Database Operations
// ============================================================================

fun SafeDatabaseOperations(appViewModel: AppViewModel) {
    // When toggling favorite
    try {
        val recipes = appViewModel.allRecipes.value
        val recipe = recipes.firstOrNull() ?: return // Safe early return

        if (recipe.id.isNotBlank()) {
            appViewModel.toggleFavorite(recipe)
        }
    } catch (e: Exception) {
        Log.e(TAG, "Database error: ${e.message}", e)
        // Handle error appropriately
    }
}

// ============================================================================
// SUMMARY: KEY SAFE PATTERNS
// ============================================================================

/**
 * 1. ELVIS OPERATOR (Provide default):
 *    val value = nullableValue ?: defaultValue
 *
 * 2. SAFE CALL OPERATOR (Optional access):
 *    nullableObject?.method()
 *
 * 3. LET FUNCTION (Execute if not null):
 *    nullableValue?.let { value -> doSomething(value) }
 *
 * 4. RUN FUNCTION (Execute scope if not null):
 *    nullableValue?.run { doSomething() } ?: doElse()
 *
 * 5. FIRST OR NULL (Safe list access):
 *    list.firstOrNull() instead of list.first()
 *
 * 6. GET OR NULL (Safe index access):
 *    list.getOrNull(index) instead of list[index]
 *
 * 7. IF NOT NULL (Check before using):
 *    if (value != null) { use(value) }
 *
 * 8. TAKE IF (Conditional filtering):
 *    value.takeIf { condition } ?: default
 *
 * 9. TRY CATCH (Handle exceptions):
 *    try { riskyOperation() } catch (e) { handleError() }
 *
 * 10. SAFE TYPING (Type checks):
 *     if (obj is String) { use(obj as String) }
 */

