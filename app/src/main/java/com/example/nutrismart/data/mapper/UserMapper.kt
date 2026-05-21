package com.example.nutrismart.data.mapper

import com.example.nutrismart.data.local.entity.UserEntity
import com.example.nutrismart.domain.model.User

fun UserEntity.toDomainModel(): User {
    return User(
        id = id,
        name = name,
        email = email,
        password = password,
        dietCategory = dietCategory,
        budget = budget,
        maxTime = maxTime
    )
}

fun User.toEntity(): UserEntity {
    return UserEntity(
        id = id,
        name = name,
        email = email,
        password = password,
        dietCategory = dietCategory,
        budget = budget,
        maxTime = maxTime
    )
}
