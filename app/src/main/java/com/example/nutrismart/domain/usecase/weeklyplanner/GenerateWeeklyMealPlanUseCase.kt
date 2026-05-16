package com.example.nutrismart.domain.usecase.weeklyplanner

import com.example.nutrismart.domain.generator.DietRecipeProvider
import com.example.nutrismart.domain.model.DayMealPlan
import com.example.nutrismart.domain.model.MealSlot
import com.example.nutrismart.domain.model.WeeklyMealPlan
import com.example.nutrismart.domain.repository.MealPlanRepository
import com.example.nutrismart.domain.repository.UserProfileRepository
import java.time.LocalDate
import java.util.UUID

class GenerateWeeklyMealPlanUseCase(
    private val mealPlanRepository: MealPlanRepository,
    private val userProfileRepository: UserProfileRepository,
    private val dietRecipeProvider: DietRecipeProvider = DietRecipeProvider()
) {
    suspend operator fun invoke(profileId: String): Result<WeeklyMealPlan> {
        return try {
            val profile = userProfileRepository.getUserProfile()
            val dietCategory = profile?.dietCategory ?: "Balanced"
            val recipes = dietRecipeProvider.getRecipes(dietCategory)

            if (recipes.isEmpty()) {
                return Result.failure(Exception("No recipes found for this diet category"))
            }

            val daysOfWeek = listOf(
                "Monday",
                "Tuesday",
                "Wednesday",
                "Thursday",
                "Friday",
                "Saturday",
                "Sunday"
            )

            val days = daysOfWeek.mapIndexed { index, dayName ->
                val breakfast = recipes[index % recipes.size]
                val lunch = recipes[(index + 1) % recipes.size]
                val dinner = recipes[(index + 2) % recipes.size]
                val snack = recipes[(index + 3) % recipes.size]

                val selectedRecipes = listOf(breakfast, lunch, dinner, snack)
                
                val dailyCost = selectedRecipes.sumOf { r ->
                    when(r.budget) {
                        "Low" -> 2.0
                        "Mid" -> 7.0
                        "High" -> 15.0
                        else -> 5.0
                    }
                }
                
                val dailyCalories = selectedRecipes.sumOf { it.calories }

                DayMealPlan(
                    dayOfWeek = dayName,
                    breakfast = MealSlot("Breakfast", breakfast.id, breakfast),
                    lunch = MealSlot("Lunch", lunch.id, lunch),
                    dinner = MealSlot("Dinner", dinner.id, dinner),
                    snack = MealSlot("Snack", snack.id, snack),
                    dailyCost = dailyCost,
                    dailyCalories = dailyCalories
                )
            }

            val mealPlan = WeeklyMealPlan(
                id = UUID.randomUUID().toString(),
                profileId = profileId,
                weekStartDate = LocalDate.now(),
                days = days,
                totalCost = days.sumOf { it.dailyCost }
            )

            Result.success(mealPlan)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
