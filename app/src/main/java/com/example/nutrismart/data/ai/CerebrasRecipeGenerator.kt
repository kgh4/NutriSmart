package com.example.nutrismart.data.ai

import android.util.Log
import com.example.nutrismart.BuildConfig
import com.example.nutrismart.domain.service.AiGeneratedRecipe
import com.example.nutrismart.domain.service.AiRecipeGenerator
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit

class CerebrasRecipeGenerator : AiRecipeGenerator {

    private companion object {
        private const val TAG = "CerebrasRecipeGenerator"
    }

    private val json = Json {
        ignoreUnknownKeys = true
        coerceInputValues = true
        isLenient = true
    }

    private val apiService: CerebrasApiService by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BuildConfig.CEREBRAS_BASE_URL)
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
            .create(CerebrasApiService::class.java)
    }

    override suspend fun generateRecipe(
        mood: String,
        dietType: String,
        budget: String,
        maxTime: Int,
        ingredients: List<String>
    ): Result<AiGeneratedRecipe> = runCatching {
        val result = generateRecipes(mood, dietType, budget, maxTime, ingredients, 1)
        result.getOrThrow().first()
    }

    suspend fun generateRecipes(
        mood: String,
        dietType: String,
        budget: String,
        maxTime: Int,
        ingredients: List<String>,
        count: Int = 3
    ): Result<List<AiGeneratedRecipe>> = try {
        Log.d(TAG, "Starting AI generation request for $count recipes...")
        
        if (BuildConfig.CEREBRAS_API_KEY.isBlank()) {
            throw Exception("Cerebras API Key is missing in local.properties")
        }

        val ingredientContext = if (ingredients.isNotEmpty()) {
            "Mandatory ingredients to use: ${ingredients.joinToString(", ")}."
        } else "Context: Suggest healthy seasonal ingredients."

        val prompt = """
            You are NutriSmart AI, a professional chef and nutritionist.
            Generate exactly $count healthy recipe(s) for someone in a $mood mood.
            Diet: $dietType, Budget: $budget, Max Prep Time: $maxTime mins.
            $ingredientContext
            
            Return ONLY a valid JSON object with this EXACT structure:
            {
              "recipes": [
                {
                  "title": "Recipe Title",
                  "description": "1-sentence hook",
                  "ingredients": ["item 1", "item 2"],
                  "steps": ["step 1", "step 2"],
                  "estimatedTime": 30,
                  "budgetLevel": "$budget",
                  "calories": 450
                }
              ]
            }
        """.trimIndent()

        val request = CerebrasChatRequest(
            model = BuildConfig.CEREBRAS_MODEL,
            messages = listOf(
                CerebrasMessage(role = "system", content = "You are a specialized nutritionist AI. You output ONLY valid JSON. No conversational text before or after the JSON."),
                CerebrasMessage(role = "user", content = prompt)
            ),
            response_format = CerebrasResponseFormat("json_object")
        )

        Log.d(TAG, "Sending request to Cerebras...")
        val response = apiService.getChatCompletions(
            authorization = "Bearer ${BuildConfig.CEREBRAS_API_KEY}",
            request = request
        )

        val rawContent = response.choices.firstOrNull()?.message?.content
        Log.d(TAG, "Raw response received: $rawContent")
        
        if (rawContent == null || rawContent.isBlank()) {
            throw Exception("Empty response from Cerebras")
        }

        // Clean JSON in case model includes markdown markers or conversational text
        val startIndex = rawContent.indexOf('{')
        val endIndex = rawContent.lastIndexOf('}')
        
        if (startIndex == -1 || endIndex == -1 || endIndex < startIndex) {
            throw Exception("Could not find a valid JSON object in the AI response.")
        }
        
        val cleanedJson = rawContent.substring(startIndex, endIndex + 1).trim()
        Log.d(TAG, "Cleaned JSON before parsing: $cleanedJson")

        val wrapper = json.decodeFromString<AiRecipeListWrapper>(cleanedJson)
        
        if (wrapper.recipes.isEmpty()) {
            throw Exception("AI returned 0 recipes")
        }

        Log.d(TAG, "Successfully parsed ${wrapper.recipes.size} recipes")
        Result.success(wrapper.recipes.map { it.toDomain() })
    } catch (e: Exception) {
        Log.e(TAG, "AI Flow Error: ${e.message}", e)
        Result.failure(e)
    }

    @Serializable
    private data class AiRecipeListWrapper(
        val recipes: List<AiRecipeDto>
    )

    @Serializable
    private data class AiRecipeDto(
        val title: String,
        val description: String,
        val ingredients: List<String>,
        val steps: List<String>,
        val estimatedTime: Int,
        val budgetLevel: String,
        val calories: Int
    )

    private fun AiRecipeDto.toDomain() = AiGeneratedRecipe(
        title = title,
        description = description,
        ingredients = ingredients,
        steps = steps,
        estimatedTime = estimatedTime,
        budgetLevel = budgetLevel,
        calories = calories,
        whyThisFitsYou = "Generated specifically for your current needs."
    )
}
