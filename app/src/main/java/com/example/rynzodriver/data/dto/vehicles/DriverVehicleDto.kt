package com.example.rynzodriver.data.dto.vehicles

// Senior explanation:
// API JSON-to-Kotlin transport objects and response field mapping.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file maps JSON API fields to Kotlin data objects.

// Serialization imports: converting JSON or structured data to/from Kotlin objects.
import com.google.gson.annotations.SerializedName

// Data model: `DriverVehicleListResponse` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class DriverVehicleListResponse(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("success") val success: Boolean = false,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("message") val message: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("data") val data: List<DriverVehicleDto> = emptyList()
)

// Data model: `DriverVehicleDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class DriverVehicleDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String = "",
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicleNumber") val vehicleNumber: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicleModel") val vehicleModel: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicleImage") val vehicleImage: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("pricePerKm") val pricePerKm: Double? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("availability") val availability: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("currentLocation") val currentLocation: DriverVehicleLocationDto? = null
)

// Data model: `DriverVehicleLocationDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class DriverVehicleLocationDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("type") val type: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("coordinates") val coordinates: List<Double> = emptyList()
)

// Data model: `DriverProfileResponse` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class DriverProfileResponse(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("success") val success: Boolean = false,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("message") val message: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("data") val data: DriverProfileDto? = null
)

// Data model: `DriverActiveTripDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class DriverActiveTripDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("activeTripId") val activeTripId: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("activeTripType") val activeTripType: String? = null
)

// Data model: `DriverProfileDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class DriverProfileDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String = "",
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("tripStatus") val tripStatus: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("availability") val availability: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("activeTrip") val activeTrip: DriverActiveTripDto? = null
)

// Data model: `ActiveTripResponse` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class ActiveTripResponse(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("success") val success: Boolean = false,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("message") val message: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("data") val data: ActiveTripDataDto? = null
)

// Data model: `ActiveTripDataDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class ActiveTripDataDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("isTripActive") val isTripActive: Boolean = false,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("activeTripId") val activeTripId: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("activeTripType") val activeTripType: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("driverId") val driverId: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicleId") val vehicleId: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("tripStatus") val tripStatus: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("companyTrip") val companyTrip: com.example.rynzodriver.data.dto.company.CompanyTripDto? = null
)
