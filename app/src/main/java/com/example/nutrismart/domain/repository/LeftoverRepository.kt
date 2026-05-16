package com.example.nutrismart.domain.repository

import com.example.nutrismart.domain.model.LeftoverInput
import com.example.nutrismart.domain.model.LeftoverRecipeResult

interface LeftoverRepository {
    suspend fun getLeftoverInput(id: String): LeftoverInput?
    suspend fun saveLeftoverInput(input: LeftoverInput)
    suspend fun deleteLeftoverInput(input: LeftoverInput)
    suspend fun getRecipesForLeftover(inputId: String): List<LeftoverRecipeResult>
    suspend fun saveResult(result: LeftoverRecipeResult)
    suspend fun deleteResult(result: LeftoverRecipeResult)
}
