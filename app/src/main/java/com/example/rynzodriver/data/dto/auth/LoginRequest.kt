package com.example.rynzodriver.data.dto.auth

// Senior explanation:
// API JSON-to-Kotlin transport objects and response field mapping.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file maps JSON API fields to Kotlin data objects.

// Serialization imports: converting JSON or structured data to/from Kotlin objects.
import com.google.gson.annotations.SerializedName

// Data model: `LoginRequest` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class LoginRequest(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("MobileNumber")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val mobileNumber: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("password")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val password: String
)
