/**
 * CRASH FIX & STABILITY IMPROVEMENTS GUIDE
 * =========================================
 * 
 * This document provides complete instructions for fixing crashes and
 * implementing null safety throughout the NutriSmart app.
 */

// ============================================================================
// 1. SAFE APP VIEW MODEL USAGE
// ============================================================================

/**
 * BEFORE (UNSAFE - CRASHES):
 * ❌ val name = user!!.name
 * ❌ val recipes = allRecipes[0]
 * ❌ val favorite = favorites.firstOrNull()!!
 */

/**
 * AFTER (SAFE - NO CRASHES):
 * ✅ val name = user?.name ?: "Unknown"
 * ✅ val recipe = allRecipes.firstOrNull()
 * ✅ val favorite = favorites.firstOrNull() ?: return
 */

// Usage Example 1: Safe ViewModel Initialization
/*
class MyActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NutriSmartTheme {
                val navController = rememberNavController()
                // SAFE: Uses SafeNutriSmartNavGraph instead of NutriSmartNavGraph
                SafeNutriSmartNavGraph(navController = navController)
            }
        }
    }
}
*/

// Usage Example 2: Safe Data Access in Compose
/*
@Composable
fun MyScreen(appViewModel: AppViewModel) {
    // SAFE: Always check for null before accessing
    val currentUser = appViewModel.currentUser.collectAsState().value
    
    if (currentUser != null) {
        Text("Welcome, ${currentUser.name}")
    } else {
        Text("Please sign in first")
    }
}
*/

// ============================================================================
// 2. NAVIGATION SAFETY
// ============================================================================

/**
 * BEFORE (UNSAFE - CRASHES ON MISSING ARGUMENTS):
 * ❌ composable(Screen.RecipeDetails.route) { backStackEntry ->
 *     val recipeId = backStackEntry.arguments?.getString("recipeId")!!
 *     RecipeDetailsScreen(recipeId = recipeId)
 * }
 */

/**
 * AFTER (SAFE - HANDLES MISSING ARGUMENTS):
 * ✅ composable(Screen.RecipeDetails.route) { backStackEntry ->
 *     val recipeId = backStackEntry.arguments?.getString("recipeId")
 *     if (recipeId?.isNotBlank() == true) {
 *         RecipeDetailsScreen(recipeId = recipeId)
 *     } else {
 *         ErrorFallbackScreen("Recipe not found")
 *     }
 * }
 */

// ============================================================================
// 3. LIST ACCESS SAFETY
// ============================================================================

/**
 * BEFORE (UNSAFE - CRASHES ON EMPTY LIST):
 * ❌ val firstRecipe = recipes[0]
 * ❌ val meal = mealPlans.first()
 */

/**
 * AFTER (SAFE - HANDLES EMPTY LISTS):
 * ✅ val firstRecipe = recipes.firstOrNull()
 * ✅ val meal = mealPlans.firstOrNull() ?: return
 * ✅ val recipe = recipes.getOrNull(index)
 */

// Usage Example: Safe List Iteration
/*
@Composable
fun RecipeList(recipes: List<Recipe>) {
    if (recipes.isEmpty()) {
        Text("No recipes available")
        return
    }
    
    LazyColumn {
        items(recipes) { recipe ->
            RecipeCard(recipe = recipe ?: return@items) // Skip null items
        }
    }
}
*/

// ============================================================================
// 4. STATE MANAGEMENT SAFETY
// ============================================================================

/**
 * BEFORE (UNSAFE):
 * ❌ var selectedPlan by mutableStateOf(dayPlans[0])
 * ❌ if (selectedPlan != null) { // might crash later }
 */

/**
 * AFTER (SAFE):
 * ✅ var selectedPlan by mutableStateOf<DayMealPlan?>(null)
 * ✅ val plan = selectedPlan ?: run { 
 *     errorMessage = "Please select a plan first"
 *     return
 * }
 */

// ============================================================================
// 5. ERROR HANDLING IN VIEWMODELS
// ============================================================================

// Safe ViewModel Function Pattern:
/*
fun loadData() {
    viewModelScope.launch {
        try {
            isLoading = true
            val data = repository.getData() ?: emptyList()
            _dataState.value = data
            clearError()
        } catch (e: Exception) {
            Log.e(TAG, "Error loading data", e)
            setError("Failed to load data: ${e.message}")
        } finally {
            isLoading = false
        }
    }
}
*/

// ============================================================================
// 6. IMAGE LOADING SAFETY
// ============================================================================

/**
 * BEFORE (UNSAFE - CRASHES IF DRAWABLE NOT FOUND):
 * ❌ Image(
 *     painter = painterResource(id = recipe.drawableId!!),
 *     contentDescription = null
 * )
 */

/**
 * AFTER (SAFE - SHOWS PLACEHOLDER):
 * ✅ Image(
 *     painter = painterResource(id = getRecipeImage(recipe.id)),
 *     contentDescription = recipe.title,
 *     modifier = Modifier.fillMaxSize(),
 *     contentScale = ContentScale.Crop
 * )
 * 
 * // In RecipeImageMapper:
 * fun getRecipeImage(recipeId: String): Int {
 *     return try {
 *         // Try to find drawable
 *         resourceId
 *     } catch (e: Exception) {
 *         R.drawable.ic_placeholder // Fallback
 *     }
 * }
 */

// ============================================================================
// 7. SHOPPING LIST SAFETY
// ============================================================================

/**
 * BEFORE (UNSAFE):
 * ❌ if (appViewModel.selectedDayPlan != null) {
 *     val plan = appViewModel.selectedDayPlan!!
 *     // Can crash if null here
 * }
 */

/**
 * AFTER (SAFE):
 * ✅ val selectedPlan = appViewModel.selectedDayPlan
 * if (selectedPlan != null) {
 *     // Safely use selectedPlan
 * } else {
 *     Text("Please select a meal plan first")
 * }
 */

// ============================================================================
// 8. LOGGING FOR DEBUGGING
// ============================================================================

/*
import android.util.Log

private const val TAG = "NutriSmart"

// Everywhere:
Log.d(TAG, "Function called with params: $param1, $param2")
Log.e(TAG, "Error occurred: ${e.message}", e)
Log.w(TAG, "Warning: null value found")
*/

// ============================================================================
// 9. IMPLEMENTATION CHECKLIST
// ============================================================================

/**
 * MIGRATION CHECKLIST - Replace old files with safe versions:
 * 
 * [ ] 1. Replace AppViewModel with SafeAppViewModel
 *        - File: presentation/viewmodel/SafeAppViewModel.kt
 *        - Has: full null safety, logging, error handling
 * 
 * [ ] 2. Replace NavGraph with SafeNutriSmartNavGraph
 *        - File: presentation/navigation/SafeNavGraph.kt
 *        - Has: safe argument extraction, error boundaries
 * 
 * [ ] 3. Fix RecipeDetailsScreen
 *        - Uses: recipe?.let {} pattern
 *        - Shows: "Recipe not found" on null
 * 
 * [ ] 4. Fix ShoppingListScreen
 *        - Removed: uiState.error!!
 *        - Added: safe error handling
 * 
 * [ ] 5. Import SafetyUtils for helper functions
 *        - File: util/SafetyUtils.kt
 *        - Use: for safe list/string operations
 * 
 * [ ] 6. Add try-catch to all API calls
 * 
 * [ ] 7. Test with null scenarios:
 *        - No user profile
 *        - No recipes loaded
 *        - No meal plan selected
 *        - Navigation with missing arguments
 * 
 * [ ] 8. Check logs for errors:
 *        - adb logcat | grep "CRASH\|ERROR"
 */

// ============================================================================
// 10. QUICK FIX TEMPLATES
// ============================================================================

// Template 1: Safe property access
/*
val value = nullableObject?.property ?: defaultValue
*/

// Template 2: Safe list operations
/*
val items = list?.takeIf { it.isNotEmpty() }?.map { it.value } ?: emptyList()
*/

// Template 3: Safe try-catch
/*
val result = try {
    riskyOperation()
} catch (e: Exception) {
    Log.e(TAG, "Error: ${e.message}", e)
    defaultValue
}
*/

// Template 4: Safe null coalescing
/*
val first = nullableValue1 ?: nullableValue2 ?: defaultValue
*/

// Template 5: Safe method call
/*
nullableObject?.run {
    this.method()
} ?: run {
    defaultBehavior()
}
*/

// ============================================================================
// 11. TESTING FOR CRASHES
// ============================================================================

/**
 * Test scenarios to ensure stability:
 * 
 * 1. EMPTY DATA TESTS:
 *    - Launch app with no recipes in DB
 *    - Verify "No data available" message shows (no crash)
 * 
 * 2. NULL USER TESTS:
 *    - Don't sign in, go to profile
 *    - Verify default values shown (no crash)
 * 
 * 3. NAVIGATION TESTS:
 *    - Navigate to RecipeDetails with invalid ID
 *    - Verify error message shown (no crash)
 * 
 * 4. LIST TESTS:
 *    - Empty recipes list → first() should be safe
 *    - Access recipes[99] → should return null not crash
 * 
 * 5. STATE TESTS:
 *    - Select plan → go to shopping
 *    - Deselect plan → show "Select plan first"
 */

// ============================================================================
// 12. USAGE IN MAIN ACTIVITY
// ============================================================================

/**
 * Update MainActivity.kt:
 * 
 * class MainActivity : ComponentActivity() {
 *     override fun onCreate(savedInstanceState: Bundle?) {
 *         super.onCreate(savedInstanceState)
 *         enableEdgeToEdge()
 *         setContent {
 *             NutriSmartTheme {
 *                 val navController = rememberNavController()
 *                 Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
 *                     SafeNutriSmartNavGraph( // Use SAFE version
 *                         navController = navController,
 *                         modifier = Modifier.padding(innerPadding)
 *                     )
 *                 }
 *             }
 *         }
 *     }
 * }
 */

// ============================================================================
// 13. MONITORING CRASHES
// ============================================================================

/**
 * Monitor errors with:
 * 
 * 1. Logcat output:
 *    adb logcat | grep -i "crash\|error\|exception"
 * 
 * 2. Add crash reporting (future enhancement):
 *    - Firebase Crashlytics
 *    - Sentry
 *    - Custom error analytics
 * 
 * 3. Check app logs:
 *    - All Log.e() calls will appear
 *    - Filter by TAG = "AppViewModel", "SafeNavGraph", etc.
 */

