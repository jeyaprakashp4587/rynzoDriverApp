package com.example.rynzodriver.domain.repository.trips

import com.example.rynzodriver.domain.model.trips.Trip
import com.example.rynzodriver.domain.model.trips.TripDetail

interface TripRepository {
    suspend fun getTripRequests(): Result<List<Trip>>
    suspend fun getTripDetails(tripId: String): Result<TripDetail>
}
