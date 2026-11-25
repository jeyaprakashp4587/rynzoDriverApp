package com.example.rynzodriver.data.dto.trips

// Senior explanation:
// API JSON-to-Kotlin transport objects and response field mapping.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file maps JSON API fields to Kotlin data objects.

// Serialization imports: converting JSON or structured data to/from Kotlin objects.
import com.google.gson.annotations.SerializedName

// Data model: `ParticularTripResponse` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class ParticularTripResponse(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("success") val success: Boolean,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("message") val message: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("data") val data: TripDetailsDto
)

// Data model: `TripDetailsDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class TripDetailsDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("status") val status: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("tripType") val tripType: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("tripStopMode") val tripStopMode: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("createdAt") val createdAt: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("stops") val stops: TripStopsDto,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("users") val users: List<TripUserDto>
)

// Data model: `TripUserDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class TripUserDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("userId") val userId: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("driverId") val driverId: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicleId") val vehicleId: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("status") val status: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("user") val user: UserCompactDto,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("driver") val driver: DriverCompactDto,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicle") val vehicle: VehicleCompactDto
)

// Data model: `UserCompactDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class UserCompactDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("Name") val name: String
)

// Data model: `DriverCompactDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class DriverCompactDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("name") val name: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("MobileNumber") val mobileNumber: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("image") val image: String
)

// Data model: `VehicleCompactDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class VehicleCompactDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicleNumber") val vehicleNumber: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicleModel") val vehicleModel: String
)
