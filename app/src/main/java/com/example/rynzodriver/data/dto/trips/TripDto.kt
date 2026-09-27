package com.example.rynzodriver.data.dto.trips

import com.google.gson.annotations.SerializedName

data class TripResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: List<TripDto>
)

data class CreatedByDto(
    @SerializedName("_id") val id: String?,
    @SerializedName("Name") val name: String?,
    @SerializedName("MobileNumber") val mobileNumber: String?
)

data class TripDto(
    @SerializedName("_id") val id: String,
    @SerializedName("status") val status: String,
    @SerializedName("tripType") val tripType: String?,
    @SerializedName("tripStopMode") val tripStopMode: String?,
    @SerializedName("tripMode") val tripMode: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("createdBy") val createdBy: CreatedByDto?,
    @SerializedName("stops") val stops: TripStopsDto
)

data class TripStopsDto(
    @SerializedName("_id") val id: String,
    @SerializedName("tripRequestId") val tripRequestId: String,
    @SerializedName("stops") val stops: List<StopDto>
)

data class StopDto(
    @SerializedName("sequence") val sequence: Int,
    @SerializedName("stopType") val stopType: String?,
    @SerializedName("locationName") val locationName: String,
    @SerializedName("coords") val coords: CoordsDto,
    @SerializedName("_id") val id: String,
    @SerializedName("recipientsMeta") val recipientsMeta: List<RecipientMetaDto>?
)

data class CoordsDto(
    @SerializedName("type") val type: String,
    @SerializedName("coordinates") val coordinates: List<Double>
)

data class RecipientMetaDto(
    @SerializedName("recipientId") val recipientId: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("contactPerson") val contactPerson: String?,
    @SerializedName("contactPhone") val contactPhone: String?,
    @SerializedName("notes") val notes: String?,
    @SerializedName("otp") val otp: String?,
    @SerializedName("arrivedAt") val arrivedAt: String?,
    @SerializedName("completedAt") val completedAt: String?
)
