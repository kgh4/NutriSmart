package com.example.nutrismart.data.ai.service

import com.example.nutrismart.domain.ai.model.SmartAiRequest
import com.example.nutrismart.domain.ai.model.SmartAiResponse
import com.example.nutrismart.domain.service.IntelligentRecipeService

/**
 * Orchestrator that tries the remote service first and falls back to local heuristic on failure.
 */
class CompositeRecipeService(
    private val remoteService: IntelligentRecipeService,
    private val localService: IntelligentRecipeService
) : IntelligentRecipeService {

    override suspend fun generateSmartRecipe(request: SmartAiRequest): Result<SmartAiResponse> {
        val remoteResult = remoteService.generateSmartRecipe(request)
        
        return if (remoteResult.isSuccess) {
            remoteResult
        } else {
            // Log the error here if needed
            localService.generateSmartRecipe(request)
        }
    }
}
