package com.example.nutrismart.data.ai.service

import com.example.nutrismart.data.ai.parser.AiJsonParser
import com.example.nutrismart.domain.ai.model.SmartAiRequest
import com.example.nutrismart.domain.ai.model.SmartAiResponse
import com.example.nutrismart.domain.service.IntelligentRecipeService
import com.example.nutrismart.util.prompt.AiPromptBuilder
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.logging.HttpLoggingInterceptor

/**
 * Cerebras AI Implementation using OpenAI-compatible Chat Completions API.
 */
class RemoteCerebrasRecipeService(
    private val apiKey: String
) : IntelligentRecipeService {

    private val client = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val json = Json { ignoreUnknownKeys = true }

    override suspend fun generateSmartRecipe(request: SmartAiRequest): Result<SmartAiResponse> = runCatching {
        if (apiKey.isBlank()) throw Exception("API key is missing")

        val prompt = AiPromptBuilder.buildRecipePrompt(request)
        
        val cerebrasRequest = CerebrasChatRequest(
            model = "llama3.1-8b", // Recommended model for speed/cost
            messages = listOf(
                CerebrasMessage(role = "system", content = "You are a professional chef and nutritionist."),
                CerebrasMessage(role = "user", content = prompt)
            ),
            response_format = CerebrasResponseFormat(type = "json_object")
        )

        val requestBody = Json.encodeToString(CerebrasChatRequest.serializer(), cerebrasRequest)
            .toRequestBody("application/json".toMediaType())

        val httpRequest = Request.Builder()
            .url("https://api.cerebras.ai/v1/chat/completions")
            .header("Authorization", "Bearer $apiKey")
            .post(requestBody)
            .build()

        client.newCall(httpRequest).execute().use { response ->
            if (!response.isSuccessful) throw Exception("Cerebras API error: ${response.code}")
            
            val responseBody = response.body?.string() ?: throw Exception("Empty response body")
            val chatResponse = json.decodeFromString<CerebrasChatResponse>(responseBody)
            val content = chatResponse.choices.firstOrNull()?.message?.content ?: throw Exception("No content in response")
            
            AiJsonParser.parseResponse(content).getOrThrow()
        }
    }
}

@Serializable
private data class CerebrasChatRequest(
    val model: String,
    val messages: List<CerebrasMessage>,
    val response_format: CerebrasResponseFormat
)

@Serializable
private data class CerebrasMessage(
    val role: String,
    val content: String
)

@Serializable
private data class CerebrasResponseFormat(
    val type: String
)

@Serializable
private data class CerebrasChatResponse(
    val choices: List<CerebrasChoice>
)

@Serializable
private data class CerebrasChoice(
    val message: CerebrasMessage
)
