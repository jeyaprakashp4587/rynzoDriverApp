package com.example.rynzodriver.domain.model.trips

data class Trip(
    val id: String,
    val status: String,
    val createdAt: String,
    val stops: List<Stop>
)

data class Stop(
    val sequence: Int,
    val type: StopType,
    val locationName: String,
    val latitude: Double,
    val longitude: Double
)

enum class StopType {
    PICKUP, DROP, UNKNOWN
}
