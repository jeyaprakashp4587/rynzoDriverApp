package com.example.rynzodriver.data.dto.trips

import com.google.gson.annotations.SerializedName

data class ParticularTripResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: TripDetailsDto
)

data class TripDetailsDto(
    @SerializedName("_id") val id: String,
    @SerializedName("status") val status: String,
    @SerializedName("tripType") val tripType: String,
    @SerializedName("tripStopMode") val tripStopMode: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("stops") val stops: TripStopsDto,
    @SerializedName("users") val users: List<TripUserDto>
)

data class TripUserDto(
    @SerializedName("userId") val userId: String,
    @SerializedName("driverId") val driverId: String,
    @SerializedName("vehicleId") val vehicleId: String,
    @SerializedName("status") val status: String,
    @SerializedName("user") val user: UserCompactDto,
    @SerializedName("driver") val driver: DriverCompactDto,
    @SerializedName("vehicle") val vehicle: VehicleCompactDto
)

data class UserCompactDto(
    @SerializedName("_id") val id: String,
    @SerializedName("Name") val name: String
)

data class DriverCompactDto(
    @SerializedName("_id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("MobileNumber") val mobileNumber: String,
    @SerializedName("image") val image: String
)

data class VehicleCompactDto(
    @SerializedName("_id") val id: String,
    @SerializedName("vehicleNumber") val vehicleNumber: String,
    @SerializedName("vehicleModel") val vehicleModel: String
)
