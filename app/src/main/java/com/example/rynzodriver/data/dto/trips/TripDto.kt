package com.example.rynzodriver.data.dto.trips

// Senior explanation:
// API JSON-to-Kotlin transport objects and response field mapping.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file maps JSON API fields to Kotlin data objects.

// Serialization imports: converting JSON or structured data to/from Kotlin objects.
import com.google.gson.annotations.SerializedName

// Data model: `TripResponse` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class TripResponse(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("success") val success: Boolean,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("message") val message: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("data") val data: List<TripDto>
)

// Data model: `CreatedByDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CreatedByDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("Name") val name: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("MobileNumber") val mobileNumber: String?
)

// Data model: `TripDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class TripDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("status") val status: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("tripType") val tripType: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("tripStopMode") val tripStopMode: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("tripMode") val tripMode: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("createdAt") val createdAt: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("createdBy") val createdBy: CreatedByDto?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("stops") val stops: TripStopsDto
)

// Data model: `TripStopsDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class TripStopsDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("tripRequestId") val tripRequestId: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("stops") val stops: List<StopDto>
)

// Data model: `StopDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class StopDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("sequence") val sequence: Int,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("stopType") val stopType: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("locationName") val locationName: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("coords") val coords: CoordsDto,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("_id") val id: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("recipientsMeta") val recipientsMeta: List<RecipientMetaDto>?
)

// Data model: `CoordsDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CoordsDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("type") val type: String,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("coordinates") val coordinates: List<Double>
)

// Data model: `RecipientMetaDto` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class RecipientMetaDto(
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("recipientId") val recipientId: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("status") val status: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("contactPerson") val contactPerson: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("contactPhone") val contactPhone: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("notes") val notes: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("otp") val otp: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("arrivedAt") val arrivedAt: String?,
// JSON mapping: Gson uses this API key instead of the Kotlin property name.
    @SerializedName("completedAt") val completedAt: String?
)
