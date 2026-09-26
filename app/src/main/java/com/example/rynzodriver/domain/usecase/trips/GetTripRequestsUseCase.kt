package com.example.rynzodriver.domain.usecase.trips

import com.example.rynzodriver.domain.model.trips.Trip
import com.example.rynzodriver.domain.repository.trips.TripRepository
import javax.inject.Inject

class GetTripRequestsUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(): Result<List<Trip>> {
        return repository.getTripRequests()
    }
}
