package com.example.rynzodriver.data.location

// Senior explanation:
// Android location updates, foreground tracking, and location state.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file manages Android location updates and location tracking state.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.location.Location
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import kotlinx.coroutines.flow.Flow

// Contract: `LocationClient` declares capabilities. Concrete classes implement these methods without changing callers.
interface LocationClient {
// Function `getLocationUpdates`: normal synchronous execution is used unless the body starts a coroutine itself.
// Asynchronous stream: `Flow` represents a sequence of values delivered over time rather than one immediate result.
    fun getLocationUpdates(interval: Long): Flow<Location>

// Runtime type: `LocationException` owns the properties and methods declared in this class body.
    class LocationException(message: String): Exception(message)
}
