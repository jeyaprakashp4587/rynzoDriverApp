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
