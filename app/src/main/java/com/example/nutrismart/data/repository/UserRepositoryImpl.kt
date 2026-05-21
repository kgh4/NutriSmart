package com.example.nutrismart.data.repository

import com.example.nutrismart.data.local.dao.UserDao
import com.example.nutrismart.data.mapper.toDomainModel
import com.example.nutrismart.data.mapper.toEntity
import com.example.nutrismart.domain.model.User
import com.example.nutrismart.domain.repository.UserRepository

class UserRepositoryImpl(
    private val userDao: UserDao
) : UserRepository {

    override suspend fun getUser(id: String): User? {
        return userDao.getUser(id)?.toDomainModel()
    }

    override suspend fun getUserByEmail(email: String): User? {
        return userDao.getUserByEmail(email)?.toDomainModel()
    }

    override suspend fun getUserProfile(): User? {
        return userDao.getUserProfile()?.toDomainModel()
    }

    override suspend fun saveUser(user: User) {
        userDao.insert(user.toEntity())
    }

    override suspend fun deleteUser(id: String) {
        userDao.deleteById(id)
    }
}

