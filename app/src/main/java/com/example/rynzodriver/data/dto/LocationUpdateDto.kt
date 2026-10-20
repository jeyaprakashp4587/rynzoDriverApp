package com.example.rynzodriver.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LocationUpdateDto(
    val type: String = "VehicleLocation",
    val vehicleId: String,
    val lat: Double,
    val lon: Double,
    val time: String = System.currentTimeMillis().toString()
)
