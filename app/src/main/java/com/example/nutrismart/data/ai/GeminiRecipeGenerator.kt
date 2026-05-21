package com.example.nutrismart.data.ai

import com.example.nutrismart.domain.service.AiGeneratedRecipe
import com.example.nutrismart.domain.service.AiRecipeGenerator
import com.google.firebase.Firebase
import com.google.firebase.vertexai.vertexAI
import com.google.firebase.vertexai.type.BlockThreshold
import com.google.firebase.vertexai.type.HarmCategory
import com.google.firebase.vertexai.type.SafetySetting
import com.google.firebase.vertexai.type.generationConfig
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable

/**
 * 2026 Android Best Practice Implementation:
 * Uses Vertex AI in Firebase (firebase-vertexai) for production-grade security.
 * This implementation is protected by Firebase App Check if configured.
 */
class GeminiRecipeGenerator : AiRecipeGenerator {

    // Using gemini-1.5-flash for speed and lower cost in 2026
    private val model = Firebase.vertexAI.generativeModel(
        modelName = "gemini-1.5-flash",
        generationConfig = generationConfig {
            responseMimeType = "application/json"
            temperature = 0.7f
        },
        safetySettings = listOf(
            SafetySetting(HarmCategory.HARASSMENT, BlockThreshold.MEDIUM_AND_ABOVE),
            SafetySetting(HarmCategory.DANGEROUS_CONTENT, BlockThreshold.MEDIUM_AND_ABOVE),
        ),
        // System instructions set the context once, reducing tokens per request
        systemInstruction = com.google.firebase.vertexai.type.content {
            text("You are NutriSmart AI, a professional nutritionist and chef. " +
                 "Always respond with a valid JSON object matching the requested schema.")
        }
    )

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
    }

    override suspend fun generateRecipe(
        mood: String,
        dietType: String,
        budget: String,
        maxTime: Int,
        ingredients: List<String>
    ): Result<AiGeneratedRecipe> = runCatching {
        val ingredientList = if (ingredients.isNotEmpty()) {
            "Include these ingredients: ${ingredients.joinToString(", ")}."
        } else "Choose fresh seasonal ingredients."

        val prompt = """
            Recipe Request:
            - Mood: $mood
            - Diet: $dietType
            - Budget: $budget
            - Prep Time: $maxTime mins
            $ingredientList
            
            Return JSON:
            {
              "name": "Title",
              "shortDescription": "1-sentence hook",
              "ingredients": ["item 1"],
              "steps": ["step 1"],
              "prepMinutes": $maxTime,
              "estimatedBudget": "$budget",
              "moodTag": "$mood",
              "whyThisFitsYou": "Explanation"
            }
        """.trimIndent()

        val response = model.generateContent(prompt)
        val responseText = response.text ?: throw Exception("AI returned no content")
        
        val dto = json.decodeFromString<AiRecipeDto>(responseText)
        dto.toDomain()
    }
}

@Serializable
private data class AiRecipeDto(
    val name: String,
    val shortDescription: String,
    val ingredients: List<String>,
    val steps: List<String>,
    val prepMinutes: Int,
    val estimatedBudget: String,
    val moodTag: String,
    val whyThisFitsYou: String
)

private fun AiRecipeDto.toDomain() = AiGeneratedRecipe(
    name = name,
    shortDescription = shortDescription,
    ingredients = ingredients,
    steps = steps,
    prepMinutes = prepMinutes,
    estimatedBudget = estimatedBudget,
    moodTag = moodTag,
    whyThisFitsYou = whyThisFitsYou
)
