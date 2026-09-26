package com.example.rynzodriver.data.api.auth

import com.example.rynzodriver.data.dto.auth.LoginRequest
import com.example.rynzodriver.data.dto.auth.LoginResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
}
