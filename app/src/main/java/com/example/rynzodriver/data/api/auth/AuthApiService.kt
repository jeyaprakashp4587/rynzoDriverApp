package com.example.rynzodriver.data.api.auth

// Senior explanation:
// Retrofit endpoint contracts and HTTP request interception.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file defines HTTP API contracts or request authentication behavior.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.dto.auth.LoginRequest
import com.example.rynzodriver.data.dto.auth.LoginResponse
import com.example.rynzodriver.data.dto.auth.RefreshTokenRequest
import com.example.rynzodriver.data.dto.auth.RefreshTokenResponse
// Retrofit imports: HTTP endpoint declarations and response handling.
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

// Contract: `AuthApiService` declares capabilities. Concrete classes implement these methods without changing callers.
interface AuthApiService {
// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
    @POST("api/auth/login")
// Function `login`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
    @POST("api/auth/refresh")
// Function `refreshToken`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun refreshToken(@Body request: RefreshTokenRequest): Response<RefreshTokenResponse>
}
