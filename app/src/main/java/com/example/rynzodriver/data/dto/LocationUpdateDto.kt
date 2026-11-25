package com.example.rynzodriver.data.dto

// Senior explanation:
// API JSON-to-Kotlin transport objects and response field mapping.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file maps JSON API fields to Kotlin data objects.

// Serialization imports: converting JSON or structured data to/from Kotlin objects.
import kotlinx.serialization.Serializable

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Serialization contract: this class can be encoded/decoded by a compatible serializer.
@Serializable
// Data model: `LocationUpdateDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class LocationUpdateDto(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val type: String = "vehicleLocation",
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val vehicleId: String,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val lat: Double,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val lon: Double,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val time: String = System.currentTimeMillis().toString()
)
