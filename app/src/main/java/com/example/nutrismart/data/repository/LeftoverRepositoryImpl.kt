package com.example.nutrismart.data.repository

import com.example.nutrismart.data.local.dao.LeftoverInputDao
import com.example.nutrismart.data.local.dao.LeftoverRecipeResultDao
import com.example.nutrismart.data.mapper.toDomainModel
import com.example.nutrismart.data.mapper.toEntity
import com.example.nutrismart.domain.model.LeftoverInput
import com.example.nutrismart.domain.model.LeftoverRecipeResult
import com.example.nutrismart.domain.repository.LeftoverRepository

class LeftoverRepositoryImpl(
    private val leftoverInputDao: LeftoverInputDao,
    private val leftoverResultDao: LeftoverRecipeResultDao
) : LeftoverRepository {

    override suspend fun getLeftoverInput(id: String): LeftoverInput? {
        return leftoverInputDao.getLeftoverInputById(id)?.toDomainModel()
    }

    override suspend fun saveLeftoverInput(input: LeftoverInput) {
        leftoverInputDao.insertLeftoverInput(input.toEntity())
    }

    override suspend fun deleteLeftoverInput(input: LeftoverInput) {
        leftoverInputDao.deleteLeftoverInput(input.toEntity())
    }

    override suspend fun getRecipesForLeftover(inputId: String): List<LeftoverRecipeResult> {
        return leftoverResultDao.getResultsForInput(inputId).map { it.toDomainModel() }
    }

    override suspend fun saveResult(result: LeftoverRecipeResult) {
        leftoverResultDao.insertResult(result.toEntity())
    }

    override suspend fun deleteResult(result: LeftoverRecipeResult) {
        leftoverResultDao.deleteResult(result.toEntity())
    }
}
