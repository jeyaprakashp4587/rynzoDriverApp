package com.example.rynzodriver.data.dto.auth

// Senior explanation:
// API JSON-to-Kotlin transport objects and response field mapping.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file maps JSON API fields to Kotlin data objects.

// Serialization imports: converting JSON or structured data to/from Kotlin objects.
import com.google.gson.annotations.SerializedName

// Data model: `LoginResponse` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class LoginResponse(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("success")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val success: Boolean,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("message")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val message: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("data")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val data: AuthDataDto
)

// Data model: `AuthDataDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class AuthDataDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("user")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val user: UserDto,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("tokens")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val tokens: TokensDto
)

// Data model: `UserDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class UserDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val id: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("Name")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val name: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("MobileNumber")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val mobileNumber: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("role")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val role: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("createdAt")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val createdAt: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("updatedAt")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val updatedAt: String
)

// Data model: `TokensDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class TokensDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("accessToken")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val accessToken: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("refreshToken")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val refreshToken: String
)

// Data model: `RefreshTokenRequest` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class RefreshTokenRequest(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("refreshToken")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val refreshToken: String
)

// Data model: `RefreshTokenResponse` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class RefreshTokenResponse(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("accessToken")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val accessToken: String
)
