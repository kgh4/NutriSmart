package com.example.nutrismart.domain.usecase.dailyideas

import com.example.nutrismart.domain.ai.AIEngine
import com.example.nutrismart.domain.model.DailyIdea
import com.example.nutrismart.domain.model.MoodType
import com.example.nutrismart.domain.model.User
import com.example.nutrismart.domain.model.Recipe

class GenerateMoodBasedDailyIdeasUseCase(private val aiEngine: AIEngine) {
    operator fun invoke(
        recipes: List<Recipe>,
        mood: MoodType,
        user: User?
    ): List<DailyIdea> {
        return aiEngine.generateMoodIdeas(
            recipes = recipes,
            mood = mood,
            dietCategory = user?.dietCategory ?: "Balanced",
            maxTime = user?.maxTime ?: 60,
            budget = when {
                (user?.budget ?: 0) < 200 -> "Low"
                (user?.budget ?: 0) < 500 -> "Mid"
                else -> "High"
            }
        )
    }
}
