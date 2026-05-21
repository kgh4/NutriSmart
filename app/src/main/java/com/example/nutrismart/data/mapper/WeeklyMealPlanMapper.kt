package com.example.nutrismart.data.mapper

import com.example.nutrismart.data.local.entity.WeeklyMealPlanEntity
import com.example.nutrismart.domain.model.DayMealPlan
import com.example.nutrismart.domain.model.MealSlot
import com.example.nutrismart.domain.model.WeeklyMealPlan
import java.time.LocalDate

fun WeeklyMealPlanEntity.toDomainModel(): WeeklyMealPlan {
    return WeeklyMealPlan(
        id = id,
        profileId = userId,
        weekStartDate = LocalDate.parse(weekStartDate),
        totalCost = totalCost,
        days = if (daysJson.isEmpty()) emptyList() else daysJson.split(";").map { dayStr ->
            val parts = dayStr.split("|")
            DayMealPlan(
                dayOfWeek = parts[0],
                breakfast = MealSlot(recipeId = parts[1].takeIf { it != "null" }),
                lunch = MealSlot(recipeId = parts[2].takeIf { it != "null" }),
                dinner = MealSlot(recipeId = parts[3].takeIf { it != "null" }),
                snack = MealSlot(recipeId = parts[4].takeIf { it != "null" }),
                dailyCost = parts[5].toDouble(),
                dailyCalories = parts[6].toInt()
            )
        }
    )
}

fun WeeklyMealPlan.toEntity(): WeeklyMealPlanEntity {
    val daysJson = days.joinToString(";") { day ->
        "${day.dayOfWeek}|${day.breakfast.recipeId ?: "null"}|${day.lunch.recipeId ?: "null"}|${day.dinner.recipeId ?: "null"}|${day.snack.recipeId ?: "null"}|${day.dailyCost}|${day.dailyCalories}"
    }
    return WeeklyMealPlanEntity(
        id = id,
        userId = profileId,
        weekStartDate = weekStartDate.toString(),
        daysJson = daysJson,
        totalCost = totalCost
    )
}
