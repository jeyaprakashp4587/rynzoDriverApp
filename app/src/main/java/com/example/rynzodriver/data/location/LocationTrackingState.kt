package com.example.rynzodriver.data.location

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class LocationUiState(
    val currentLocation: String = "Lat: --, Lon: --",
    val lastLocation: String = "Lat: --, Lon: --"
)

object LocationTrackingState {
    private val _state = MutableStateFlow(LocationUiState())
    val state: StateFlow<LocationUiState> = _state.asStateFlow()

    fun updateLocation(lat: Double, lon: Double) {
        val current = _state.value
        _state.value = current.copy(
            lastLocation = current.currentLocation,
            currentLocation = "Lat: $lat, Lon: $lon"
        )
    }

    fun reset() {
        _state.value = LocationUiState()
    }
}
