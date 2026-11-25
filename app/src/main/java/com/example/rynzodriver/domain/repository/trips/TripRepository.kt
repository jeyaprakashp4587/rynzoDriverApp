package com.example.rynzodriver.domain.repository.trips

// Senior explanation:
// Framework-independent repository contracts.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file declares domain repository contracts.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.domain.model.trips.Trip
import com.example.rynzodriver.domain.model.trips.TripDetail

// Contract: `TripRepository` declares capabilities. Concrete classes implement these methods without changing callers.
interface TripRepository {
// Function `getTripRequests`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun getTripRequests(): Result<List<Trip>>
// Function `getTripDetails`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun getTripDetails(tripId: String): Result<TripDetail>
// Function `acceptTripRequest`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun acceptTripRequest(tripId: String): Result<String>
}
