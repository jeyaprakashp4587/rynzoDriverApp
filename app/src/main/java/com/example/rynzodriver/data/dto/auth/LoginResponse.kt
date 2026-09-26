package com.example.rynzodriver.data.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("data")
    val data: AuthDataDto
)

data class AuthDataDto(
    @SerializedName("user")
    val user: UserDto,
    @SerializedName("tokens")
    val tokens: TokensDto
)

data class UserDto(
    @SerializedName("_id")
    val id: String,
    @SerializedName("Name")
    val name: String,
    @SerializedName("MobileNumber")
    val mobileNumber: String,
    @SerializedName("role")
    val role: String,
    @SerializedName("createdAt")
    val createdAt: String,
    @SerializedName("updatedAt")
    val updatedAt: String
)

data class TokensDto(
    @SerializedName("accessToken")
    val accessToken: String,
    @SerializedName("refreshToken")
    val refreshToken: String
)
