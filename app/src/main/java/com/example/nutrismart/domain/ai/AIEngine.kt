package com.example.nutrismart.domain.ai

import com.example.nutrismart.domain.model.DailyIdea
import com.example.nutrismart.domain.model.MoodType
import com.example.nutrismart.domain.model.Recipe
import kotlin.random.Random

/**
 * AIEngine handles intelligent recipe selection and filtering
 * - Weekly meal planning
 * - Daily recipe suggestions
 * - Leftover ingredient matching
 */
class AIEngine {

    /**
     * Mood-aware Daily Ideas AI
     */
    fun generateMoodIdeas(
        recipes: List<Recipe>,
        mood: MoodType,
        dietCategory: String,
        maxTime: Int,
        budget: String
    ): List<DailyIdea> {
        val filtered = recipes.filter { recipe ->
            recipe.dietCategory.equals(dietCategory, ignoreCase = true) &&
            isBudgetMatching(recipe.budget, budget)
        }

        val moodMatches = when (mood) {
            MoodType.COMFORT -> filtered.filter { 
                it.calories > 400 || 
                it.ingredients.contains("cheese", ignoreCase = true) ||
                it.ingredients.contains("harissa", ignoreCase = true) ||
                it.ingredients.contains("merguez", ignoreCase = true) ||
                it.ingredients.contains("couscous", ignoreCase = true)
            }
            MoodType.ENERGETIC -> filtered.filter { it.calories in 300..600 }
            MoodType.LIGHT -> filtered.filter { it.calories < 350 }
            MoodType.FOCUS -> filtered.filter { 
                it.ingredients.contains("fish", ignoreCase = true) || 
                it.ingredients.contains("tuna", ignoreCase = true) ||
                it.ingredients.contains("nuts", ignoreCase = true) || 
                it.ingredients.contains("olive oil", ignoreCase = true) ||
                it.calories < 500 
            }
            MoodType.QUICK -> filtered.filter { it.time <= 20 }
            MoodType.SURPRISE -> filtered.shuffled()
        }.shuffled().take(6)

        return moodMatches.map { recipe ->
            DailyIdea(
                recipe = recipe,
                moodTitle = generateMoodTitle(recipe.title, mood)
            )
        }
    }

    private fun generateMoodTitle(originalTitle: String, mood: MoodType): String {
        return when (mood) {
            MoodType.COMFORT -> "🧸 Cozy $originalTitle"
            MoodType.ENERGETIC -> "⚡ Power-Fuel: $originalTitle"
            MoodType.LIGHT -> "🥗 Fresh & Zesty $originalTitle"
            MoodType.FOCUS -> "🧠 Brain-Booster $originalTitle"
            MoodType.QUICK -> "⏱️ Snap-and-Eat $originalTitle"
            MoodType.SURPRISE -> "🎁 Wonder-Bowl: $originalTitle"
        }
    }

    /**
     * Weekly Planner AI: Filter recipes and rotate meals
     * @param recipes All available recipes
     * @param dietCategory User's dietary preference
     * @param maxTime Maximum cooking time in minutes
     * @param budget Maximum budget (Low, Mid, High)
     * @return List of recipes suitable for weekly planning
     */
    fun generateWeeklyRecipes(
        recipes: List<Recipe>,
        dietCategory: String,
        maxTime: Int,
        budget: String
    ): List<Recipe> {
        return recipes
            .filter { recipe ->
                // Filter by diet category
                recipe.dietCategory.equals(dietCategory, ignoreCase = true) &&
                // Filter by time
                recipe.time <= maxTime &&
                // Filter by budget
                isBudgetMatching(recipe.budget, budget)
            }
            .sortedWith(compareBy({ it.time }, { it.calories }))
    }

    /**
     * Daily Ideas AI: Return 4-6 random recipes based on constraints
     * @param recipes All available recipes
     * @param dietCategory User's dietary preference
     * @param maxTime Maximum cooking time
     * @param budget Maximum budget
     * @return 4-6 diverse recipes
     */
    fun generateDailyIdeas(
        recipes: List<Recipe>,
        dietCategory: String,
        maxTime: Int,
        budget: String
    ): List<Recipe> {
        val filtered = generateWeeklyRecipes(recipes, dietCategory, maxTime, budget)

        // Ensure we always return a varied set by shuffling
        return filtered.shuffled().take(6)
    }

    /**
     * Leftover AI: Match recipes by ingredients and rank by score
     * @param availableIngredients List of available ingredients
     * @param recipes All recipes
     * @return Recipes ranked by number of matching ingredients
     */
    fun findRecipesByLeftovers(
        availableIngredients: List<String>,
        recipes: List<Recipe>
    ): List<Recipe> {
        if (availableIngredients.isEmpty()) return emptyList()

        // Normalize available ingredients
        val normalizedAvailable = availableIngredients
            .map { it.lowercase().trim() }
            .filter { it.isNotBlank() }

        return recipes
            .map { recipe ->
                // Split recipe ingredients (handle both \n and comma)
                val recipeIngredientsList = recipe.ingredients
                    .split("\n", ",")
                    .map { it.lowercase().trim() }
                    .filter { it.isNotBlank() }

                // Calculate score: how many available ingredients are found in this recipe?
                // Or: how many of this recipe's ingredients are available?
                val matches = recipeIngredientsList.filter { recIng ->
                    normalizedAvailable.any { avail -> 
                        recIng.contains(avail) || avail.contains(recIng)
                    }
                }
                
                val score = matches.size
                recipe to score
            }
            .filter { it.second > 0 } // Only recipes with matches
            .sortedByDescending { it.second } // Sort by match count (highest score first)
            .map { it.first }
    }

    /**
     * Get randomized recipes for variety (avoid repetition)
     * @param recipes Available recipes
     * @param previousRecipeIds Recipes used in the last 3 days
     * @param count Number of recipes to return
     * @return Recipes not in previous list
     */
    fun getRecipesWithVariety(
        recipes: List<Recipe>,
        previousRecipeIds: List<String>,
        count: Int = 7
    ): List<Recipe> {
        val newRecipes = recipes.filter { it.id !in previousRecipeIds }

        return if (newRecipes.size >= count) {
            newRecipes.shuffled().take(count)
        } else {
            // If not enough new recipes, include some previous ones
            (newRecipes + recipes.filter { it.id in previousRecipeIds }.shuffled())
                .shuffled()
                .take(count)
        }
    }

    /**
     * Check if recipe budget matches user budget
     */
    private fun isBudgetMatching(recipeBudget: String, userBudget: String): Boolean {
        val budgetOrder = mapOf("Low" to 1, "Mid" to 2, "High" to 3)
        val recipeBudgetLevel = budgetOrder[recipeBudget] ?: 2
        val userBudgetLevel = budgetOrder[userBudget] ?: 2

        return recipeBudgetLevel <= userBudgetLevel
    }

    /**
     * Calculate recipe score based on multiple factors
     */
    fun scoreRecipe(
        recipe: Recipe,
        userDietCategory: String,
        maxTime: Int,
        maxCalories: Int,
        userBudget: String
    ): Float {
        var score = 0f

        // Diet match: 40 points
        if (recipe.dietCategory.equals(userDietCategory, ignoreCase = true)) {
            score += 40f
        }

        // Time efficiency: 30 points (lower time = higher score)
        val timeScore = ((maxTime - recipe.time).toFloat() / maxTime) * 30
        score += maxOf(0f, timeScore)

        // Calorie balance: 20 points
        val calorieScore = if (recipe.calories <= maxCalories) 20f else 0f
        score += calorieScore

        // Budget: 10 points
        if (isBudgetMatching(recipe.budget, userBudget)) {
            score += 10f
        }

        return score
    }
}

