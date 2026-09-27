package com.example.rynzodriver.domain.usecase.trips

import com.example.rynzodriver.domain.model.trips.TripDetail
import com.example.rynzodriver.domain.repository.trips.TripRepository
import javax.inject.Inject

class GetTripDetailsUseCase @Inject constructor(
    private val repository: TripRepository
) {
    suspend operator fun invoke(tripId: String): Result<TripDetail> {
        return repository.getTripDetails(tripId)
    }
}
