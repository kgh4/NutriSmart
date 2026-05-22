package com.example.nutrismart.data.ai

import android.util.Log
// Don't directly reference BuildConfig fields that may not be present at compile time in this module.
// We'll read them reflectively with safe defaults so the compiler won't fail when fields are missing.
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

        val baseUrl = getBuildConfigString("CEREBRAS_BASE_URL", "https://api.cerebras.ai/v1/")

        Retrofit.Builder()
            .baseUrl(baseUrl)
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
        
        val apiKey = getBuildConfigString("CEREBRAS_API_KEY", "").orEmpty()
        if (apiKey.isBlank()) {
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
            model = getBuildConfigString("CEREBRAS_MODEL", "llama3.1-8b"),
            messages = listOf(
                CerebrasMessage(role = "system", content = "You are a specialized nutritionist AI. You output ONLY valid JSON. No conversational text before or after the JSON."),
                CerebrasMessage(role = "user", content = prompt)
            ),
            response_format = CerebrasResponseFormat("json_object")
        )

        Log.d(TAG, "Sending request to Cerebras...")
        val response = apiService.getChatCompletions(
            authorization = "Bearer $apiKey",
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

    private fun getBuildConfigString(name: String, default: String = ""): String {
        return try {
            val cls = Class.forName("com.example.nutrismart.BuildConfig")
            val field = cls.getDeclaredField(name)
            field.isAccessible = true
            (field.get(null) as? String) ?: default
        } catch (t: Throwable) {
            Log.w(TAG, "BuildConfig field $name not found, using default")
            default
        }
    }
}
