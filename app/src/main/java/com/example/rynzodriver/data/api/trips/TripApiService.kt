package com.example.rynzodriver.data.api.trips

import com.example.rynzodriver.data.dto.trips.ParticularTripResponse
import com.example.rynzodriver.data.dto.trips.TripResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface TripApiService {
    @GET("api/trips/requests")
    suspend fun getRequestTrips(): Response<TripResponse>

    @GET("api/trips/requests/{tripId}")
    suspend fun getParticularRequestedTripDetails(@Path("tripId") tripId: String): Response<ParticularTripResponse>
}
