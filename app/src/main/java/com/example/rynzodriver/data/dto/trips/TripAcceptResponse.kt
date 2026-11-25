package com.example.rynzodriver.data.dto.trips

// Senior explanation:
// API JSON-to-Kotlin transport objects and response field mapping.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file maps JSON API fields to Kotlin data objects.

// Serialization imports: converting JSON or structured data to/from Kotlin objects.
import com.google.gson.annotations.SerializedName

// Data model: `TripAcceptResponse` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class TripAcceptResponse(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("success") val success: Boolean,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("message") val message: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("data") val data: Any? = null
)
