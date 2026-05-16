package com.example.nutrismart.di

import android.content.Context
import com.example.nutrismart.data.local.db.NutriSmartDatabase
import com.example.nutrismart.data.local.entity.RecipeEntity
import com.example.nutrismart.data.repository.*
import com.example.nutrismart.domain.repository.*
import com.example.nutrismart.domain.usecase.dailyideas.GenerateDailyMealIdeasUseCase
import com.example.nutrismart.domain.usecase.profile.GetUserProfileUseCase
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
    
    val getUserProfileUseCase: GetUserProfileUseCase
    val generateWeeklyMealPlanUseCase: GenerateWeeklyMealPlanUseCase
    val generateDailyMealIdeasUseCase: GenerateDailyMealIdeasUseCase
    val generateShoppingListUseCase: GenerateShoppingListUseCase
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

    override val getUserProfileUseCase: GetUserProfileUseCase by lazy {
        GetUserProfileUseCase(userProfileRepository)
    }

    override val generateWeeklyMealPlanUseCase: GenerateWeeklyMealPlanUseCase by lazy {
        GenerateWeeklyMealPlanUseCase(mealPlanRepository, userProfileRepository)
    }

    override val generateDailyMealIdeasUseCase: GenerateDailyMealIdeasUseCase by lazy {
        GenerateDailyMealIdeasUseCase(recipeRepository, userProfileRepository)
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
                val recipes = listOf(
                    RecipeEntity(
                        id = 1,
                        title = "Cheese Omelette",
                        description = "Simple vegetarian breakfast",
                        ingredients = "2 eggs, Salt, Butter, Cheese",
                        steps = "Beat eggs\nCook in pan\nAdd cheese\nServe",
                        mealType = "Breakfast",
                        dietType = "Vegetarian",
                        dietCategory = "Vegetarian",
                        estimatedCost = 2.5,
                        estimatedCalories = 350,
                        prepMinutes = 10,
                        isFavorite = 0,
                        sourceType = "LOCAL"
                    ),
                    RecipeEntity(
                        id = 2,
                        title = "Tuna Sandwich",
                        description = "Quick pescatarian lunch",
                        ingredients = "Bread, Tuna, Mayonnaise",
                        steps = "Mix tuna\nFill bread\nServe",
                        mealType = "Lunch",
                        dietType = "Pescatarian",
                        dietCategory = "Pescatarian",
                        estimatedCost = 3.0,
                        estimatedCalories = 300,
                        prepMinutes = 5,
                        isFavorite = 0,
                        sourceType = "LOCAL"
                    ),
                    RecipeEntity(
                        id = 3,
                        title = "Vegan Chickpea Curry",
                        description = "Healthy vegan dinner",
                        ingredients = "Chickpeas, Coconut milk, Curry powder, Rice",
                        steps = "Sauté spices\nAdd chickpeas and milk\nSimmer\nServe with rice",
                        mealType = "Dinner",
                        dietType = "Vegan",
                        dietCategory = "Vegan",
                        estimatedCost = 4.0,
                        estimatedCalories = 450,
                        prepMinutes = 20,
                        isFavorite = 0,
                        sourceType = "LOCAL"
                    ),
                    RecipeEntity(
                        id = 4,
                        title = "Chicken Protein Bowl",
                        description = "High-protein meal",
                        ingredients = "Chicken breast, Quinoa, Broccoli",
                        steps = "Grill chicken\nCook quinoa\nSteam broccoli\nCombine",
                        mealType = "Lunch",
                        dietType = "High-Protein",
                        dietCategory = "High-Protein",
                        estimatedCost = 5.5,
                        estimatedCalories = 500,
                        prepMinutes = 15,
                        isFavorite = 0,
                        sourceType = "LOCAL"
                    ),
                    RecipeEntity(
                        id = 5,
                        title = "Greek Salad",
                        description = "Fresh mediterranean salad",
                        ingredients = "Cucumber, Tomato, Feta, Olives",
                        steps = "Chop veggies\nAdd feta and olives\nDrizzle olive oil",
                        mealType = "Lunch",
                        dietType = "Mediterranean",
                        dietCategory = "Mediterranean",
                        estimatedCost = 3.5,
                        estimatedCalories = 250,
                        prepMinutes = 10,
                        isFavorite = 0,
                        sourceType = "LOCAL"
                    )
                )
                recipeDao.insertAll(recipes)
            }
        }
    }
}
