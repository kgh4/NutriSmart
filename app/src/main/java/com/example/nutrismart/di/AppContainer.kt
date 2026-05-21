package com.example.nutrismart.di

import android.content.Context
import com.example.nutrismart.data.local.db.NutriSmartDatabase
import com.example.nutrismart.data.local.entity.RecipeEntity
import com.example.nutrismart.data.mapper.toEntity
import com.example.nutrismart.data.repository.*
import com.example.nutrismart.domain.repository.*
import com.example.nutrismart.domain.usecase.dailyideas.GenerateDailyMealIdeasUseCase
import com.example.nutrismart.domain.usecase.dailyideas.GenerateMoodBasedDailyIdeasUseCase
import com.example.nutrismart.domain.usecase.dailyideas.GenerateAiDailyIdeasUseCase
import com.example.nutrismart.domain.service.AiRecipeGenerator
import com.example.nutrismart.data.ai.GeminiRecipeGenerator
import com.example.nutrismart.domain.usecase.profile.GetUserProfileUseCase
import com.example.nutrismart.domain.usecase.profile.SaveUserProfileUseCase
import com.example.nutrismart.domain.usecase.shoppinglist.GenerateShoppingListUseCase
import com.example.nutrismart.domain.usecase.weeklyplanner.GenerateWeeklyMealPlanUseCase
import com.example.nutrismart.domain.generator.LeftoverRecipeGenerator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

interface AppContainer {
    val userProfileRepository: UserProfileRepository
    val recipeRepository: RecipeRepository
    val mealPlanRepository: MealPlanRepository
    val shoppingListRepository: ShoppingListRepository
    val leftoverRepository: LeftoverRepository
    
    // New repositories for local database
    val userRepository: UserRepository
    val favoriteRepository: FavoriteRepository
    val dayMealPlanRepository: DayMealPlanRepository

    val getUserProfileUseCase: GetUserProfileUseCase
    val saveUserProfileUseCase: SaveUserProfileUseCase
    val generateWeeklyMealPlanUseCase: GenerateWeeklyMealPlanUseCase
    val generateDailyMealIdeasUseCase: GenerateDailyMealIdeasUseCase
    val generateMoodBasedDailyIdeasUseCase: GenerateMoodBasedDailyIdeasUseCase
    val generateAiDailyIdeasUseCase: GenerateAiDailyIdeasUseCase
    val generateShoppingListUseCase: GenerateShoppingListUseCase
    val aiRecipeGenerator: AiRecipeGenerator
    val leftoverRecipeGenerator: LeftoverRecipeGenerator
}

class AppDataContainer(private val context: Context) : AppContainer {

    private val database: NutriSmartDatabase by lazy {
        NutriSmartDatabase.getDatabase(context)
    }

    override val userProfileRepository: UserProfileRepository by lazy {
        UserProfileRepositoryImpl(database.userProfileDao())
    }

    override val recipeRepository: RecipeRepository by lazy {
        RecipeRepositoryImpl(database.recipeDao())
    }

    override val mealPlanRepository: MealPlanRepository by lazy {
        MealPlanRepositoryImpl(database.weeklyMealPlanDao())
    }

    override val shoppingListRepository: ShoppingListRepository by lazy {
        ShoppingListRepositoryImpl(database.shoppingListDao())
    }

    override val leftoverRepository: LeftoverRepository by lazy {
        LeftoverRepositoryImpl(database.leftoverInputDao(), database.leftoverRecipeResultDao())
    }

    override val userRepository: UserRepository by lazy {
        UserRepositoryImpl(database.userDao())
    }

    override val favoriteRepository: FavoriteRepository by lazy {
        FavoriteRepositoryImpl(database.favoriteDao())
    }

    override val dayMealPlanRepository: DayMealPlanRepository by lazy {
        DayMealPlanRepositoryImpl(database.mealPlanDao())
    }

    override val getUserProfileUseCase: GetUserProfileUseCase by lazy {
        GetUserProfileUseCase(userProfileRepository)
    }

    override val saveUserProfileUseCase: SaveUserProfileUseCase by lazy {
        SaveUserProfileUseCase(userProfileRepository)
    }

    override val generateWeeklyMealPlanUseCase: GenerateWeeklyMealPlanUseCase by lazy {
        GenerateWeeklyMealPlanUseCase(mealPlanRepository, userProfileRepository)
    }

    override val generateDailyMealIdeasUseCase: GenerateDailyMealIdeasUseCase by lazy {
        GenerateDailyMealIdeasUseCase(recipeRepository, userProfileRepository)
    }

    override val generateMoodBasedDailyIdeasUseCase: GenerateMoodBasedDailyIdeasUseCase by lazy {
        GenerateMoodBasedDailyIdeasUseCase(com.example.nutrismart.domain.ai.AIEngine())
    }

    override val aiRecipeGenerator: AiRecipeGenerator by lazy {
        // In 2026, no API key is passed here.
        // Vertex AI in Firebase uses the configuration from google-services.json
        // and is secured via Firebase App Check.
        GeminiRecipeGenerator()
    }

    override val generateAiDailyIdeasUseCase: GenerateAiDailyIdeasUseCase by lazy {
        GenerateAiDailyIdeasUseCase(
            aiRecipeGenerator = aiRecipeGenerator,
            fallbackUseCase = generateMoodBasedDailyIdeasUseCase,
            recipeRepository = recipeRepository
        )
    }

    override val generateShoppingListUseCase: GenerateShoppingListUseCase by lazy {
        GenerateShoppingListUseCase(shoppingListRepository, mealPlanRepository)
    }
    
    override val leftoverRecipeGenerator: LeftoverRecipeGenerator by lazy {
        LeftoverRecipeGenerator()
    }

    init {
        seedDatabase()
    }

    private fun seedDatabase() {
        CoroutineScope(Dispatchers.IO).launch {
            val recipeDao = database.recipeDao()
            val existing = recipeDao.getAllRecipes()

            if (existing.isEmpty()) {
                val provider = com.example.nutrismart.domain.generator.DietRecipeProvider()
                val recipes = provider.getRecipes("All")
                recipes.forEach { recipe ->
                    recipeDao.insert(recipe.toEntity())
                }
            }
        }
    }
}
