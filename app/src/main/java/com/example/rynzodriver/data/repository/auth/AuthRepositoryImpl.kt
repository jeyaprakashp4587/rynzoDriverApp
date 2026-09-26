package com.example.rynzodriver.data.repository.auth

import com.example.rynzodriver.data.api.auth.AuthApiService
import com.example.rynzodriver.data.dto.auth.LoginRequest
import com.example.rynzodriver.data.local.DataStoreManager
import com.example.rynzodriver.domain.model.User
import com.example.rynzodriver.domain.repository.auth.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val authApiService: AuthApiService,
    private val dataStoreManager: DataStoreManager
) : AuthRepository {

    override suspend fun login(mobileNumber: String, password: String): Result<User> {
        return try {
            val response = authApiService.login(LoginRequest(mobileNumber, password))
            if (response.isSuccessful && response.body() != null) {
                val loginResponse = response.body()!!
                val userData = loginResponse.data.user
                val tokens = loginResponse.data.tokens

                dataStoreManager.saveAuthData(
                    id = userData.id,
                    name = userData.name,
                    mobileNumber = userData.mobileNumber,
                    role = userData.role,
                    accessToken = tokens.accessToken,
                    refreshToken = tokens.refreshToken
                )

                Result.success(
                    User(
                        id = userData.id,
                        name = userData.name,
                        mobileNumber = userData.mobileNumber,
                        role = userData.role,
                        token = tokens.accessToken
                    )
                )
            } else {
                Result.failure(Exception(response.body()?.message ?: "Login failed"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun isLoggedIn(): Flow<Boolean> {
        return dataStoreManager.accessToken.map { !it.isNullOrEmpty() }
    }
}
