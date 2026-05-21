package com.example.nutrismart.domain.service

import com.example.nutrismart.domain.ai.model.SmartAiRequest
import com.example.nutrismart.domain.ai.model.SmartAiResponse

/**
 * Core interface for AI-powered recipe generation logic.
 * Abstracted to support multiple providers (Gemini, Local, Mocks).
 */
interface IntelligentRecipeService {
    suspend fun generateSmartRecipe(request: SmartAiRequest): Result<SmartAiResponse>
}
