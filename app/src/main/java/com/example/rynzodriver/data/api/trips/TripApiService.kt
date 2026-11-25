package com.example.rynzodriver.data.api.trips

// Senior explanation:
// Retrofit endpoint contracts and HTTP request interception.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file defines HTTP API contracts or request authentication behavior.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.dto.trips.ParticularTripResponse
import com.example.rynzodriver.data.dto.trips.TripAcceptResponse
import com.example.rynzodriver.data.dto.trips.TripResponse
// Retrofit imports: HTTP endpoint declarations and response handling.
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

// Contract: `TripApiService` declares capabilities. Concrete classes implement these methods without changing callers.
interface TripApiService {
// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
    @GET("api/trips/requests")
// Function `getRequestTrips`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun getRequestTrips(): Response<TripResponse>

// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
    @GET("api/trips/requests/{tripId}")
// Function `getParticularRequestedTripDetails`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun getParticularRequestedTripDetails(@Path("tripId") tripId: String): Response<ParticularTripResponse>

// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
    @POST("api/trips/requests/{tripId}/accept-driver")
// Function `acceptTripRequest`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun acceptTripRequest(@Path("tripId") tripId: String): Response<TripAcceptResponse>
}
