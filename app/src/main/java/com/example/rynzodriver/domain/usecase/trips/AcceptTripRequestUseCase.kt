package com.example.rynzodriver.domain.usecase.trips

import com.example.rynzodriver.domain.repository.trips.TripRepository
import javax.inject.Inject

class AcceptTripRequestUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(tripId: String): Result<String> {
        return repository.acceptTripRequest(tripId)
    }
}
