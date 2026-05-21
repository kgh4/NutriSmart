package com.example.nutrismart.presentation.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.domain.ai.AIEngine
import com.example.nutrismart.domain.model.MoodType
import com.example.nutrismart.domain.model.DayMealPlan
import com.example.nutrismart.domain.model.Favorite
import com.example.nutrismart.domain.model.MealPlan
import com.example.nutrismart.domain.model.WeeklyMealPlan
import com.example.nutrismart.domain.model.Recipe
import com.example.nutrismart.domain.model.User
import com.example.nutrismart.domain.repository.DayMealPlanRepository
import com.example.nutrismart.domain.repository.FavoriteRepository
import com.example.nutrismart.domain.repository.RecipeRepository
import com.example.nutrismart.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * SAFE AppViewModel with comprehensive null safety and crash prevention
 *
 * Key improvements:
 * ✅ NO !! operator anywhere
 * ✅ Safe null checks with ?: and ?. operators
 * ✅ Try-catch blocks for all critical operations
 * ✅ Defensive copying and validation
 * ✅ Safe list access with firstOrNull() and isEmpty()
 * ✅ Comprehensive logging for debugging
 */
class AppViewModel(
    private val userRepository: UserRepository,
    private val recipeRepository: RecipeRepository,
    private val favoriteRepository: FavoriteRepository,
    private val dayMealPlanRepository: DayMealPlanRepository
) : ViewModel() {

    private companion object {
        private const val TAG = "AppViewModel"
        private const val DEFAULT_USER_NAME = "Guest"
        private const val DEFAULT_USER_EMAIL = "guest@example.com"
        private const val DEFAULT_MAX_TIME = 60
        private const val DEFAULT_BUDGET = 0
        private const val DEFAULT_DIET = "Balanced"
    }

    private val aiEngine = AIEngine()

    // ===== User State =====
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    var userName by mutableStateOf(DEFAULT_USER_NAME)
    var userEmail by mutableStateOf(DEFAULT_USER_EMAIL)

    // ===== Recipes State =====
    private val _allRecipes = MutableStateFlow<List<Recipe>>(emptyList())
    val allRecipes: StateFlow<List<Recipe>> = _allRecipes.asStateFlow()

    private val _dailyIdeas = MutableStateFlow<List<Recipe>>(emptyList())
    val dailyIdeas: StateFlow<List<Recipe>> = _dailyIdeas.asStateFlow()

    // ===== Favorites State =====
    val savedRecipes = mutableStateListOf<Recipe>()

    private val _favoriteRecipeIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteRecipeIds: StateFlow<Set<String>> = _favoriteRecipeIds.asStateFlow()

    // ===== Meal Plan State =====
    var selectedWeeklyPlan by mutableStateOf<WeeklyMealPlan?>(null)

    var selectedDayPlan by mutableStateOf<DayMealPlan?>(null)
        private set

    var selectedMood by mutableStateOf(MoodType.COMFORT)
        private set

    fun updateMood(mood: MoodType) {
        selectedMood = mood
    }

    private val _mealPlans = MutableStateFlow<List<MealPlan>>(emptyList())
    val mealPlans: StateFlow<List<MealPlan>> = _mealPlans.asStateFlow()

    // ===== Loading & Error States =====
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf("")
        private set

    init {
        loadInitialData()
    }

    /**
     * Safe initialization of all data with proper error handling
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                isLoading = true
                Log.d(TAG, "Starting initial data load...")

                // Load recipes first as they are static-ish
                loadRecipes()
                
                // Then load user and their specific data
                loadUser()
                if (_currentUser.value != null) {
                    loadFavorites()
                }

                Log.d(TAG, "Initial data load completed successfully")
            } catch (e: Exception) {
                handleError("Error loading initial data", e)
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Safe recipe loading with empty list fallback
     */
    fun loadRecipes() {
        viewModelScope.launch {
            try {
                val recipes = recipeRepository.getAllRecipes()
                    ?: emptyList() // Safe fallback
                _allRecipes.value = recipes.filterNotNull() // Remove any null entries
                Log.d(TAG, "Loaded ${recipes.size} recipes")
            } catch (e: Exception) {
                handleError("Error loading recipes", e)
                // Keep existing recipes on error
            }
        }
    }

    /**
     * Safe user profile loading
     */
    fun loadUser() {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserProfile()
                if (user != null) {
                    _currentUser.value = user
                    // Safe name assignment
                    userName = user.name.takeIf { it.isNotBlank() } ?: DEFAULT_USER_NAME
                    userEmail = user.email.takeIf { it.isNotBlank() } ?: DEFAULT_USER_EMAIL
                    Log.d(TAG, "User loaded: ${user.name}")
                } else {
                    Log.d(TAG, "No user profile found")
                    setDefaultUser()
                }
            } catch (e: Exception) {
                handleError("Error loading user", e)
                setDefaultUser()
            }
        }
    }

    /**
     * Set default user when none exists
     */
    private fun setDefaultUser() {
        userName = DEFAULT_USER_NAME
        userEmail = DEFAULT_USER_EMAIL
        _currentUser.value = null
    }

    /**
     * Safe sign up with validation
     */
    fun signUp(name: String, email: String, diet: String, password: String = "") {
        viewModelScope.launch {
            try {
                // Validate inputs
                val safeName = name.takeIf { it.isNotBlank() } ?: run {
                    setError("Name cannot be empty")
                    return@launch
                }
                val safeEmail = email.takeIf { it.isNotBlank() } ?: run {
                    setError("Email cannot be empty")
                    return@launch
                }
                val safeDiet = diet.takeIf { it.isNotBlank() } ?: DEFAULT_DIET

                val newUser = User(
                    id = java.util.UUID.randomUUID().toString(),
                    name = safeName,
                    email = safeEmail,
                    password = password,
                    dietCategory = safeDiet
                )

                saveUser(newUser)
                _currentUser.value = newUser
                Log.d(TAG, "Sign up successful for: $safeName")
            } catch (e: Exception) {
                handleError("Sign up failed", e)
            }
        }
    }

    /**
     * Safe sign in with email validation
     */
    fun signIn(email: String, password: String = "") {
        viewModelScope.launch {
            try {
                val safeEmail = email.takeIf { it.isNotBlank() } ?: run {
                    setError("Email cannot be empty")
                    return@launch
                }

                val user = userRepository.signIn(safeEmail, password)
                if (user != null) {
                    _currentUser.value = user
                    userName = user.name.takeIf { it.isNotBlank() } ?: DEFAULT_USER_NAME
                    userEmail = user.email.takeIf { it.isNotBlank() } ?: DEFAULT_USER_EMAIL
                    clearError()
                    Log.d(TAG, "Sign in successful")
                } else {
                    setError("Invalid email or password")
                    Log.w(TAG, "Sign in failed for: $safeEmail")
                }
            } catch (e: Exception) {
                handleError("Sign in failed", e)
            }
        }
    }

    /**
     * Safe sign out - clears user state
     */
    fun signOut() {
        viewModelScope.launch {
            try {
                _currentUser.value = null
                setDefaultUser()
                selectedDayPlan = null
                selectedWeeklyPlan = null
                savedRecipes.clear()
                _favoriteRecipeIds.value = emptySet()
                Log.d(TAG, "User signed out successfully")
            } catch (e: Exception) {
                handleError("Error during sign out", e)
            }
        }
    }

    /**
     * Deduct spent amount from weekly budget
     */
    fun recordPurchase(amount: Double) {
        viewModelScope.launch {
            try {
                val profile = userRepository.getUserProfile() // Get current user
                if (profile != null) {
                    // Update user's budget locally in the UserProfile too if needed
                    // For now we update the UI state and should persist it
                    val currentProfile = _currentUser.value
                    if (currentProfile != null) {
                        val newBudget = (currentProfile.budget - amount.toInt()).coerceAtLeast(0)
                        val updatedUser = currentProfile.copy(budget = newBudget)
                        saveUser(updatedUser)
                        Log.d(TAG, "Deducted $amount from budget. New budget: $newBudget")
                    }
                }
            } catch (e: Exception) {
                handleError("Failed to deduct from budget", e)
            }
        }
    }
    fun saveUser(user: User) {
        viewModelScope.launch {
            try {
                if (user.name.isBlank()) {
                    setError("User name cannot be empty")
                    return@launch
                }

                userRepository.saveUser(user)
                _currentUser.value = user
                userName = user.name
                userEmail = user.email
                clearError()
                Log.d(TAG, "User saved successfully")
            } catch (e: Exception) {
                handleError("Error saving user", e)
            }
        }
    }

    /**
     * Select a weekly plan to be used for shopping list generation
     */
    fun selectWeeklyPlan(plan: WeeklyMealPlan) {
        selectedWeeklyPlan = plan
        Log.d(TAG, "Weekly plan selected: ${plan.id}")
    }

    /**
     * Safe day plan selection and saving
     */
    fun useThisDayPlan(plan: DayMealPlan) {
        viewModelScope.launch {
            try {
                selectedDayPlan = plan

                // Safe recipe ID extraction with fallbacks
                val mealPlan = MealPlan(
                    id = 1,
                    day = plan.dayOfWeek.takeIf { it.isNotBlank() } ?: "Monday",
                    breakfastId = plan.breakfast?.recipeId?.takeIf { it.isNotBlank() } ?: "",
                    lunchId = plan.lunch?.recipeId?.takeIf { it.isNotBlank() } ?: "",
                    dinnerId = plan.dinner?.recipeId?.takeIf { it.isNotBlank() } ?: "",
                    snackId = plan.snack?.recipeId?.takeIf { it.isNotBlank() } ?: ""
                )

                saveMealPlan(mealPlan)
                Log.d(TAG, "Day plan selected: ${plan.dayOfWeek}")
            } catch (e: Exception) {
                handleError("Failed to save day plan", e)
                selectedDayPlan = null // Reset on error
            }
        }
    }

    /**
     * Safe favorites loading with join operation
     */
    fun loadFavorites() {
        viewModelScope.launch {
            try {
                val userId = _currentUser.value?.id ?: ""
                if (userId.isBlank()) return@launch

                val favorites = recipeRepository.getFavoriteRecipes(userId) ?: emptyList()

                savedRecipes.clear()
                savedRecipes.addAll(favorites.filterNotNull())

                _favoriteRecipeIds.value = favorites
                    .filterNotNull()
                    .map { it.id }
                    .filter { it.isNotBlank() }
                    .toSet()

                Log.d(TAG, "Loaded ${savedRecipes.size} favorite recipes")
            } catch (e: Exception) {
                handleError("Error loading favorites", e)
                // Keep existing favorites on error
            }
        }
    }

    /**
     * Safe favorite toggle with immediate UI update
     */
    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            try {
                val userId = _currentUser.value?.id ?: ""
                if (recipe.id.isBlank() || userId.isBlank()) {
                    setError("Invalid recipe ID or User not logged in")
                    return@launch
                }

                val isCurrentlyFavorite = recipe.id in _favoriteRecipeIds.value

                if (isCurrentlyFavorite) {
                    // Remove from database
                    favoriteRepository.deleteFavorite(recipe.id, userId)
                    _favoriteRecipeIds.value = _favoriteRecipeIds.value - recipe.id
                    savedRecipes.removeAll { it.id == recipe.id }
                    Log.d(TAG, "Recipe removed from favorites: ${recipe.title}")
                } else {
                    // Save to database
                    favoriteRepository.saveFavorite(Favorite(recipeId = recipe.id, userId = userId))
                    _favoriteRecipeIds.value = _favoriteRecipeIds.value + recipe.id
                    if (savedRecipes.none { it.id == recipe.id }) {
                        savedRecipes.add(recipe)
                    }
                    Log.d(TAG, "Recipe added to favorites: ${recipe.title}")
                }
                clearError()
            } catch (e: Exception) {
                handleError("Error toggling favorite", e)
            }
        }
    }

    /**
     * Safe favorite check
     */
    fun isFavorite(recipeId: String): Boolean {
        return recipeId.isNotBlank() && recipeId in _favoriteRecipeIds.value
    }

    /**
     * Safe daily ideas generation
     */
    fun generateDailyIdeas(
        dietCategory: String? = _currentUser.value?.dietCategory,
        maxTime: Int? = _currentUser.value?.maxTime,
        budget: String? = null
    ) {
        viewModelScope.launch {
            try {
                val safeDiet = dietCategory.takeIf { !it.isNullOrBlank() } ?: DEFAULT_DIET
                val safeMaxTime = maxTime?.takeIf { it > 0 } ?: DEFAULT_MAX_TIME

                val safeBudget = budget ?: when {
                    (_currentUser.value?.budget ?: DEFAULT_BUDGET) < 200 -> "Low"
                    (_currentUser.value?.budget ?: DEFAULT_BUDGET) < 500 -> "Mid"
                    else -> "High"
                }

                val recipes = _allRecipes.value.takeIf { it.isNotEmpty() } ?: emptyList()
                val ideas = aiEngine.generateDailyIdeas(
                    recipes,
                    safeDiet,
                    safeMaxTime,
                    safeBudget
                )

                _dailyIdeas.value = ideas.filterNotNull()
                clearError()
                Log.d(TAG, "Generated ${ideas.size} daily ideas")
            } catch (e: Exception) {
                handleError("Error generating daily ideas", e)
                _dailyIdeas.value = emptyList()
            }
        }
    }

    /**
     * Safe weekly plan generation with variety
     */
    fun generateWeeklyPlan(
        dietCategory: String? = _currentUser.value?.dietCategory,
        maxTime: Int? = _currentUser.value?.maxTime,
        budget: String? = null
    ): List<Recipe> {
        return try {
            val safeDiet = dietCategory.takeIf { !it.isNullOrBlank() } ?: DEFAULT_DIET
            val safeMaxTime = maxTime?.takeIf { it > 0 } ?: DEFAULT_MAX_TIME

            val safeBudget = budget ?: when {
                (_currentUser.value?.budget ?: DEFAULT_BUDGET) < 200 -> "Low"
                (_currentUser.value?.budget ?: DEFAULT_BUDGET) < 500 -> "Mid"
                else -> "High"
            }

            val recipes = _allRecipes.value.takeIf { it.isNotEmpty() } ?: emptyList()
            val weeklyRecipes = aiEngine.generateWeeklyRecipes(
                recipes,
                safeDiet,
                safeMaxTime,
                safeBudget
            )

            Log.d(TAG, "Generated weekly plan with ${weeklyRecipes.size} recipes")
            weeklyRecipes.filterNotNull()
        } catch (e: Exception) {
            handleError("Error generating weekly plan", e)
            emptyList()
        }
    }

    /**
     * Safe leftover recipes finding
     */
    fun findRecipesByLeftovers(ingredients: List<String>): List<Recipe> {
        return try {
            if (ingredients.isEmpty()) {
                setError("Please provide at least one ingredient")
                return emptyList()
            }

            val safeIngredients = ingredients
                .filterNotNull()
                .filter { it.isNotBlank() }

            val recipes = _allRecipes.value.takeIf { it.isNotEmpty() } ?: emptyList()
            val results = aiEngine.findRecipesByLeftovers(safeIngredients, recipes)

            clearError()
            Log.d(TAG, "Found ${results.size} recipes from leftovers")
            results.filterNotNull()
        } catch (e: Exception) {
            handleError("Error finding leftover recipes", e)
            emptyList()
        }
    }

    /**
     * Safe variety recipes with repetition avoidance
     */
    fun getRecipesWithVariety(
        previousRecipeIds: List<String>,
        count: Int = 7
    ): List<Recipe> {
        return try {
            if (count <= 0) {
                setError("Count must be greater than 0")
                return emptyList()
            }

            val safePreviousIds = previousRecipeIds.filterNotNull().filter { it.isNotBlank() }
            val recipes = _allRecipes.value.takeIf { it.isNotEmpty() } ?: emptyList()
            val results = aiEngine.getRecipesWithVariety(recipes, safePreviousIds, count)

            clearError()
            Log.d(TAG, "Generated ${results.size} variety recipes")
            results.filterNotNull()
        } catch (e: Exception) {
            handleError("Error getting variety recipes", e)
            emptyList()
        }
    }

    /**
     * Safe meal plans loading
     */
    fun loadMealPlans() {
        viewModelScope.launch {
            try {
                val plans = dayMealPlanRepository.getAllMealPlans() ?: emptyList()
                _mealPlans.value = plans.filterNotNull()
                Log.d(TAG, "Loaded ${plans.size} meal plans")
            } catch (e: Exception) {
                handleError("Error loading meal plans", e)
                // Keep existing plans on error
            }
        }
    }

    /**
     * Safe meal plan saving
     */
    fun saveMealPlan(mealPlan: MealPlan) {
        viewModelScope.launch {
            try {
                if (mealPlan.day.isBlank()) {
                    setError("Meal plan day cannot be empty")
                    return@launch
                }

                dayMealPlanRepository.saveMealPlan(mealPlan)
                loadMealPlans()
                clearError()
                Log.d(TAG, "Meal plan saved for: ${mealPlan.day}")
            } catch (e: Exception) {
                handleError("Error saving meal plan", e)
            }
        }
    }

    /**
     * Safe meal plan deletion
     */
    fun deleteMealPlan(id: Int) {
        viewModelScope.launch {
            try {
                if (id <= 0) {
                    setError("Invalid meal plan ID")
                    return@launch
                }

                dayMealPlanRepository.deleteMealPlan(id)
                if (selectedDayPlan != null) {
                    selectedDayPlan = null
                }
                loadMealPlans()
                Log.d(TAG, "Meal plan deleted: ID $id")
            } catch (e: Exception) {
                handleError("Error deleting meal plan", e)
            }
        }
    }

    // ===== Error & State Management =====

    /**
     * Set error message safely
     */
    private fun setError(message: String) {
        val safeMessage = message.takeIf { it.isNotBlank() } ?: "An unknown error occurred"
        errorMessage = safeMessage
        Log.e(TAG, safeMessage)
    }

    /**
     * Clear error message
     */
    fun clearError() {
        errorMessage = ""
    }

    /**
     * Centralized error handling
     */
    private fun handleError(context: String, exception: Exception) {
        val message = exception.message?.takeIf { it.isNotBlank() }
            ?: "An unknown error occurred"
        Log.e(TAG, "$context: $message", exception)
        setError("$context: $message")
    }
}
