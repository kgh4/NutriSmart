package com.example.nutrismart.presentation.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.nutrismart.domain.ai.AIEngine
import com.example.nutrismart.domain.model.DayMealPlan
import com.example.nutrismart.domain.model.Favorite
import com.example.nutrismart.domain.model.MealPlan
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

class AppViewModel(
    private val userRepository: UserRepository,
    private val recipeRepository: RecipeRepository,
    private val favoriteRepository: FavoriteRepository,
    private val dayMealPlanRepository: DayMealPlanRepository
) : ViewModel() {

    private val aiEngine = AIEngine()

    // ===== User State =====
    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    var userName by mutableStateOf("Student")
    var userEmail by mutableStateOf("alex.chen@university.edu")

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
    var selectedDayPlan by mutableStateOf<DayMealPlan?>(null)

    private val _mealPlans = MutableStateFlow<List<MealPlan>>(emptyList())
    val mealPlans: StateFlow<List<MealPlan>> = _mealPlans.asStateFlow()

    // ===== Loading States =====
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf("")

    init {
        loadInitialData()
    }

    /**
     * Load all initial data
     */
    private fun loadInitialData() {
        viewModelScope.launch {
            try {
                isLoading = true
                loadRecipes()
                loadFavorites()
                loadUser()
            } catch (e: Exception) {
                errorMessage = "Error loading data: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    /**
     * Load all recipes from database
     */
    fun loadRecipes() {
        viewModelScope.launch {
            try {
                val recipes = recipeRepository.getAllRecipes()
                _allRecipes.value = recipes
            } catch (e: Exception) {
                errorMessage = "Error loading recipes: ${e.message}"
            }
        }
    }

    /**
     * Load user profile
     */
    fun loadUser() {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserProfile()
                _currentUser.value = user
                if (user != null) {
                    userName = user.name
                    userEmail = user.email
                }
            } catch (e: Exception) {
                errorMessage = "Error loading user: ${e.message}"
            }
        }
    }

    /**
     * Auth: Sign up a new user
     */
    fun signUp(name: String, email: String, diet: String) {
        viewModelScope.launch {
            try {
                val newUser = User(
                    id = java.util.UUID.randomUUID().toString(),
                    name = name,
                    email = email,
                    dietCategory = diet
                )
                saveUser(newUser)
                _currentUser.value = newUser
            } catch (e: Exception) {
                errorMessage = "Sign up failed: ${e.message}"
            }
        }
    }

    /**
     * Auth: Sign in with email
     */
    fun signIn(email: String) {
        viewModelScope.launch {
            try {
                val user = userRepository.getUserByEmail(email)
                if (user != null) {
                    _currentUser.value = user
                    userName = user.name
                    userEmail = user.email
                } else {
                    errorMessage = "User not found"
                }
            } catch (e: Exception) {
                errorMessage = "Sign in failed: ${e.message}"
            }
        }
    }

    /**
     * Save currently selected day plan to DB
     */
    fun useThisDayPlan(plan: DayMealPlan) {
        viewModelScope.launch {
            try {
                selectedDayPlan = plan
                // Save to MealPlanEntity (id = 1 for current active plan)
                val mealPlan = MealPlan(
                    id = 1,
                    day = plan.dayOfWeek,
                    breakfastId = plan.breakfast.recipeId ?: "",
                    lunchId = plan.lunch.recipeId ?: "",
                    dinnerId = plan.dinner.recipeId ?: "",
                    snackId = plan.snack.recipeId ?: ""
                )
                saveMealPlan(mealPlan)
            } catch (e: Exception) {
                errorMessage = "Failed to save day plan: ${e.message}"
            }
        }
    }

    /**
     * Save user to database
     */
    fun saveUser(user: User) {
        viewModelScope.launch {
            try {
                userRepository.saveUser(user)
                _currentUser.value = user
                userName = user.name
                userEmail = user.email
            } catch (e: Exception) {
                errorMessage = "Error saving user: ${e.message}"
            }
        }
    }

    /**
     * Load favorites from database using a JOIN
     */
    fun loadFavorites() {
        viewModelScope.launch {
            try {
                // Use the repository method that performs the database join
                val favorites = recipeRepository.getFavoriteRecipes()
                savedRecipes.clear()
                savedRecipes.addAll(favorites)
                
                // Update the IDs set for quick UI lookups
                _favoriteRecipeIds.value = favorites.map { it.id }.toSet()
            } catch (e: Exception) {
                errorMessage = "Error loading favorites: ${e.message}"
            }
        }
    }

    /**
     * Toggle favorite status of a recipe using database
     */
    fun toggleFavorite(recipe: Recipe) {
        viewModelScope.launch {
            try {
                val isCurrentlyFavorite = recipe.id in _favoriteRecipeIds.value

                if (isCurrentlyFavorite) {
                    // Remove from database
                    favoriteRepository.deleteFavorite(recipe.id)
                    // Immediate UI update
                    _favoriteRecipeIds.value = _favoriteRecipeIds.value - recipe.id
                    savedRecipes.removeAll { it.id == recipe.id }
                } else {
                    // Save to database
                    favoriteRepository.saveFavorite(Favorite(recipeId = recipe.id))
                    // Immediate UI update
                    _favoriteRecipeIds.value = _favoriteRecipeIds.value + recipe.id
                    if (savedRecipes.none { it.id == recipe.id }) {
                        savedRecipes.add(recipe)
                    }
                }
            } catch (e: Exception) {
                errorMessage = "Error toggling favorite: ${e.message}"
            }
        }
    }

    /**
     * Check if recipe is favorite
     */
    fun isFavorite(recipeId: String): Boolean {
        return recipeId in _favoriteRecipeIds.value
    }

    /**
     * Get daily ideas based on user constraints
     */
    fun generateDailyIdeas(
        dietCategory: String = _currentUser.value?.dietCategory ?: "Balanced",
        maxTime: Int = _currentUser.value?.maxTime ?: 60,
        budget: String = when {
            (_currentUser.value?.budget ?: 0) < 200 -> "Low"
            (_currentUser.value?.budget ?: 0) < 500 -> "Mid"
            else -> "High"
        }
    ) {
        viewModelScope.launch {
            try {
                val ideas = aiEngine.generateDailyIdeas(
                    _allRecipes.value,
                    dietCategory,
                    maxTime,
                    budget
                )
                _dailyIdeas.value = ideas
            } catch (e: Exception) {
                errorMessage = "Error generating daily ideas: ${e.message}"
            }
        }
    }

    /**
     * Get recipes for weekly planning
     */
    fun generateWeeklyPlan(
        dietCategory: String = _currentUser.value?.dietCategory ?: "Balanced",
        maxTime: Int = _currentUser.value?.maxTime ?: 60,
        budget: String = when {
            (_currentUser.value?.budget ?: 0) < 200 -> "Low"
            (_currentUser.value?.budget ?: 0) < 500 -> "Mid"
            else -> "High"
        }
    ): List<Recipe> {
        return aiEngine.generateWeeklyRecipes(
            _allRecipes.value,
            dietCategory,
            maxTime,
            budget
        )
    }

    /**
     * Find recipes based on leftover ingredients
     */
    fun findRecipesByLeftovers(ingredients: List<String>): List<Recipe> {
        return aiEngine.findRecipesByLeftovers(ingredients, _allRecipes.value)
    }

    /**
     * Get recipes for variety (avoid repetition)
     */
    fun getRecipesWithVariety(
        previousRecipeIds: List<String>,
        count: Int = 7
    ): List<Recipe> {
        return aiEngine.getRecipesWithVariety(_allRecipes.value, previousRecipeIds, count)
    }

    /**
     * Load meal plans
     */
    fun loadMealPlans() {
        viewModelScope.launch {
            try {
                val plans = dayMealPlanRepository.getAllMealPlans()
                _mealPlans.value = plans
            } catch (e: Exception) {
                errorMessage = "Error loading meal plans: ${e.message}"
            }
        }
    }

    /**
     * Save meal plan
     */
    fun saveMealPlan(mealPlan: MealPlan) {
        viewModelScope.launch {
            try {
                dayMealPlanRepository.saveMealPlan(mealPlan)
                loadMealPlans()
            } catch (e: Exception) {
                errorMessage = "Error saving meal plan: ${e.message}"
            }
        }
    }

    /**
     * Delete meal plan
     */
    fun deleteMealPlan(id: Int) {
        viewModelScope.launch {
            try {
                dayMealPlanRepository.deleteMealPlan(id)
                loadMealPlans()
            } catch (e: Exception) {
                errorMessage = "Error deleting meal plan: ${e.message}"
            }
        }
    }
}
