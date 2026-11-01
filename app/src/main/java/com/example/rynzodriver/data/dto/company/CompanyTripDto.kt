package com.example.rynzodriver.data.dto.company

import com.google.gson.annotations.SerializedName

data class CompanyTripListResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: List<CompanyTripDto> = emptyList()
)

data class CompanyTripDto(
    @SerializedName("_id") val id: String = "",
    @SerializedName("routeId") val routeId: String? = null,
    @SerializedName("routeName") val routeName: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("status") val status: String? = null,
    @SerializedName("tripType") val tripType: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("tripStopMode") val tripStopMode: String? = null,
    @SerializedName("isDailyTrip") val isDailyTrip: Boolean? = null,
    @SerializedName("customerName") val customerName: String? = null,
    @SerializedName("driverId") val driverId: String? = null,
    @SerializedName("vehicleId") val vehicleId: String? = null,
    @SerializedName("vehicle") val vehicle: CompanyTripVehicleDto? = null,
    @SerializedName("recipientCount") val recipientCount: Int? = null,
    @SerializedName("recipients") val recipients: List<CompanyTripRecipientDto> = emptyList(),
    @SerializedName("stops") val stops: List<CompanyTripStopDto> = emptyList(),
    @SerializedName("pickupLocation") val pickupLocation: CompanyTripStopLocationDto? = null,
    @SerializedName("dropoffLocation") val dropoffLocation: CompanyTripStopLocationDto? = null,
    @SerializedName("createdAt") val createdAt: String? = null
)

data class CompanyTripVehicleDto(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("vehicleNumber") val vehicleNumber: String? = null,
    @SerializedName("vehicleModel") val vehicleModel: String? = null
)

data class CompanyTripRecipientDto(
    @SerializedName("_id") val id: String? = null,
    @SerializedName("customerName") val customerName: String? = null,
    @SerializedName("recipientName") val recipientName: String? = null,
    @SerializedName("name") val name: String? = null
)

data class CompanyTripStopDto(
    @SerializedName("locationName") val locationName: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("type") val type: String? = null,
    @SerializedName("sequence") val sequence: Int? = null,
    @SerializedName("stopType") val stopType: String? = null
)

data class CompanyTripStopLocationDto(
    @SerializedName("name") val name: String? = null,
    @SerializedName("locationName") val locationName: String? = null,
    @SerializedName("address") val address: String? = null
)

data class CompanyRunStartRequest(
    @SerializedName("vehicleId") val vehicleId: String
)

data class CompanyRunStartResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: CompanyRunStartData? = null
)

data class CompanyRunStartData(
    @SerializedName("tripId") val tripId: String? = null,
    @SerializedName("routeId") val routeId: String? = null,
    @SerializedName("vehicleId") val vehicleId: String? = null,
    @SerializedName("status") val status: String? = null
)
