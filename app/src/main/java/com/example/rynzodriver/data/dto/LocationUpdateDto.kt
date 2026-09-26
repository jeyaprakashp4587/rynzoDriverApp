package com.example.rynzodriver.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class LocationUpdateDto(
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long = System.currentTimeMillis()
)
