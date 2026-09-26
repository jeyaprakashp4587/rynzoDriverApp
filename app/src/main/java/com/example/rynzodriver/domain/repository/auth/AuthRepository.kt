package com.example.rynzodriver.domain.repository.auth

import com.example.rynzodriver.domain.model.User
import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    suspend fun login(mobileNumber: String, password: String): Result<User>
    fun isLoggedIn(): Flow<Boolean>
}
