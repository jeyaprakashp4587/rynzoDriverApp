package com.example.rynzodriver.domain.model.trips

data class TripDetail(
    val id: String,
    val status: String,
    val tripType: String?,
    val createdAt: String,
    val customerName: String?,
    val customerPhone: String?,
    val driverName: String?,
    val driverPhone: String?,
    val vehicleNumber: String?,
    val vehicleModel: String?,
    val stops: List<Stop>
)
