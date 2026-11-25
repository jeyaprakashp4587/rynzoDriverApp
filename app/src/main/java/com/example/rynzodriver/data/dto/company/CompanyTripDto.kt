package com.example.rynzodriver.data.dto.company

// Senior explanation:
// API JSON-to-Kotlin transport objects and response field mapping.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file maps JSON API fields to Kotlin data objects.

// Serialization imports: converting JSON or structured data to/from Kotlin objects.
import com.google.gson.annotations.SerializedName

// Data model: `CompanyTripListResponse` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CompanyTripListResponse(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("success") val success: Boolean = false,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("message") val message: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("data") val data: List<CompanyTripDto> = emptyList()
)

// Data model: `CompanyTripDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CompanyTripDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String = "",
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("routeId") val routeId: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("routeName") val routeName: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("title") val title: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("name") val name: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("status") val status: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("tripType") val tripType: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("type") val type: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("tripStopMode") val tripStopMode: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("isDailyTrip") val isDailyTrip: Boolean? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("customerName") val customerName: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("driverId") val driverId: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicleId") val vehicleId: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicle") val vehicle: CompanyTripVehicleDto? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("recipientCount") val recipientCount: Int? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("recipients") val recipients: List<CompanyTripRecipientDto> = emptyList(),
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("stops") val stops: List<CompanyTripStopDto> = emptyList(),
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("pickupLocation") val pickupLocation: CompanyTripStopLocationDto? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("dropoffLocation") val dropoffLocation: CompanyTripStopLocationDto? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("createdAt") val createdAt: String? = null
)

// Data model: `CompanyTripVehicleDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CompanyTripVehicleDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicleNumber") val vehicleNumber: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicleModel") val vehicleModel: String? = null
)

// Data model: `CompanyTripRecipientDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CompanyTripRecipientDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("customerName") val customerName: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("recipientName") val recipientName: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("name") val name: String? = null
)

// Data model: `CompanyTripStopDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CompanyTripStopDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("locationName") val locationName: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("name") val name: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("type") val type: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("sequence") val sequence: Int? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("stopType") val stopType: String? = null
)

// Data model: `CompanyTripStopLocationDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CompanyTripStopLocationDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("name") val name: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("locationName") val locationName: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("address") val address: String? = null
)

// Data model: `CompanyRunStartRequest` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CompanyRunStartRequest(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicleId") val vehicleId: String
)

// Data model: `CompanyRunStartResponse` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CompanyRunStartResponse(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("success") val success: Boolean = false,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("message") val message: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("data") val data: CompanyRunStartData? = null
)

// Data model: `CompanyRunStartData` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CompanyRunStartData(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("tripId") val tripId: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("routeId") val routeId: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("vehicleId") val vehicleId: String? = null,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("status") val status: String? = null
)
