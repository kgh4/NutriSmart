package com.example.nutrismart.data.ai

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.POST

interface CerebrasApiService {
    @POST("chat/completions")
    suspend fun getChatCompletions(
        @Header("Authorization") authorization: String,
        @Body request: CerebrasChatRequest
    ): CerebrasChatResponse
}

@Serializable
data class CerebrasChatRequest(
    val model: String,
    val messages: List<CerebrasMessage>,
    val response_format: CerebrasResponseFormat? = null,
    val temperature: Float = 0.7f
)

@Serializable
data class CerebrasMessage(
    val role: String,
    val content: String
)

@Serializable
data class CerebrasResponseFormat(
    val type: String
)

@Serializable
data class CerebrasChatResponse(
    val choices: List<CerebrasChoice>
)

@Serializable
data class CerebrasChoice(
    val message: CerebrasMessage
)
