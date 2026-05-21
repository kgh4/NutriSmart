package com.example.nutrismart.data.ai.parser

import com.example.nutrismart.domain.ai.model.SmartAiResponse
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString

/**
 * Handles cleaning and safe parsing of potentially messy AI JSON output.
 */
object AiJsonParser {
    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    fun parseResponse(rawText: String): Result<SmartAiResponse> = runCatching {
        // Clean up markdown noise
        val cleanedJson = rawText.trim()
            .removePrefix("```json")
            .removePrefix("```")
            .removeSuffix("```")
            .trim()
            
        val dto = json.decodeFromString<AiResponseDto>(cleanedJson)
        dto.toDomain()
    }
}

@Serializable
private data class AiResponseDto(
    val title: String,
    val description: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val mealType: String = "Lunch",
    val calories: Int = 0,
    val timeMinutes: Int = 0,
    val budgetLevel: String = "Medium",
    val dietCategory: String = "Balanced",
    val estimatedCost: Double = 0.0,
    val difficulty: String = "Medium",
    val substitutions: Map<String, String> = emptyMap(),
    val whyItFits: String = "",
    val shoppingItems: List<String> = emptyList(),
    val warnings: List<String> = emptyList()
)

private fun AiResponseDto.toDomain() = SmartAiResponse(
    title = title,
    description = description,
    ingredients = ingredients,
    steps = steps,
    mealType = mealType,
    calories = calories,
    timeMinutes = timeMinutes,
    budgetLevel = budgetLevel,
    dietCategory = dietCategory,
    estimatedCost = estimatedCost,
    difficulty = difficulty,
    substitutions = substitutions,
    whyItFits = whyItFits,
    shoppingItems = shoppingItems,
    warnings = warnings
)
