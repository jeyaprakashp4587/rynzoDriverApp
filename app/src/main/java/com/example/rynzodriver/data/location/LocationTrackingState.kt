package com.example.rynzodriver.data.location

// Senior explanation:
// Android location updates, foreground tracking, and location state.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file manages Android location updates and location tracking state.

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Data model: `LocationUiState` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class LocationUiState(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val currentLocation: String = "Lat: --, Lon: --",
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val lastLocation: String = "Lat: --, Lon: --"
)

// Singleton object: `LocationTrackingState` has one process-wide instance; initialization occurs the first time it is accessed.
object LocationTrackingState {
// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
    private val _state = MutableStateFlow(LocationUiState())
// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Read-only state exposure: callers can collect but should not mutate this flow; mutation remains inside the owning class.
    val state: StateFlow<LocationUiState> = _state.asStateFlow()

// Function `updateLocation`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun updateLocation(lat: Double, lon: Double) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val current = _state.value
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
        _state.value = current.copy(
            lastLocation = current.currentLocation,
            currentLocation = "Lat: $lat, Lon: $lon"
        )
    }

// Function `reset`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun reset() {
        _state.value = LocationUiState()
    }
}
