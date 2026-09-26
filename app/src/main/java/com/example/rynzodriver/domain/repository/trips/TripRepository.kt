package com.example.rynzodriver.domain.repository.trips

import com.example.rynzodriver.domain.model.trips.Trip

interface TripRepository {
    suspend fun getTripRequests(): Result<List<Trip>>
}
