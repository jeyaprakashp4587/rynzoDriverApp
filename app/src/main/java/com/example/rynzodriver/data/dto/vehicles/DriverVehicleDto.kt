package com.example.rynzodriver.data.dto.vehicles

import com.google.gson.annotations.SerializedName

data class DriverVehicleListResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: List<DriverVehicleDto> = emptyList()
)

data class DriverVehicleDto(
    @SerializedName("_id") val id: String = "",
    @SerializedName("vehicleNumber") val vehicleNumber: String? = null,
    @SerializedName("vehicleModel") val vehicleModel: String? = null,
    @SerializedName("vehicleImage") val vehicleImage: String? = null,
    @SerializedName("pricePerKm") val pricePerKm: Double? = null,
    @SerializedName("availability") val availability: String? = null,
    @SerializedName("currentLocation") val currentLocation: DriverVehicleLocationDto? = null
)

data class DriverVehicleLocationDto(
    @SerializedName("type") val type: String? = null,
    @SerializedName("coordinates") val coordinates: List<Double> = emptyList()
)

data class DriverProfileResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: DriverProfileDto? = null
)

data class DriverActiveTripDto(
    @SerializedName("activeTripId") val activeTripId: String? = null,
    @SerializedName("activeTripType") val activeTripType: String? = null
)

data class DriverProfileDto(
    @SerializedName("_id") val id: String = "",
    @SerializedName("tripStatus") val tripStatus: String? = null,
    @SerializedName("availability") val availability: String? = null,
    @SerializedName("activeTrip") val activeTrip: DriverActiveTripDto? = null
)

data class ActiveTripResponse(
    @SerializedName("success") val success: Boolean = false,
    @SerializedName("message") val message: String? = null,
    @SerializedName("data") val data: ActiveTripDataDto? = null
)

data class ActiveTripDataDto(
    @SerializedName("isTripActive") val isTripActive: Boolean = false,
    @SerializedName("activeTripId") val activeTripId: String? = null,
    @SerializedName("activeTripType") val activeTripType: String? = null,
    @SerializedName("driverId") val driverId: String? = null,
    @SerializedName("vehicleId") val vehicleId: String? = null,
    @SerializedName("tripStatus") val tripStatus: String? = null,
    @SerializedName("companyTrip") val companyTrip: com.example.rynzodriver.data.dto.company.CompanyTripDto? = null
)
