package com.example.rynzodriver.data.repository.trips

import com.example.rynzodriver.data.api.trips.TripApiService
import com.example.rynzodriver.domain.model.trips.Stop
import com.example.rynzodriver.domain.model.trips.StopType
import com.example.rynzodriver.domain.model.trips.Trip
import com.example.rynzodriver.domain.repository.trips.TripRepository
import javax.inject.Inject

class TripRepositoryImpl @Inject constructor(
    private val tripApiService: TripApiService
) : TripRepository {

    override suspend fun getTripRequests(): Result<List<Trip>> {
        return try {
            val response = tripApiService.getRequestTrips()
            if (response.isSuccessful && response.body() != null) {
                val tripDtos = response.body()!!.data
                val trips = tripDtos.map { dto ->
                    Trip(
                        id = dto.id,
                        status = dto.status,
                        createdAt = dto.createdAt,
                        tripType = dto.tripType,
                        tripMode = dto.tripMode,
                        customerName = dto.createdBy?.name ?: "Unknown Customer",
                        customerPhone = dto.createdBy?.mobileNumber ?: "",
                        stops = dto.stops.stops.map { stopDto ->
                            Stop(
                                sequence = stopDto.sequence,
                                type = when (stopDto.stopType?.lowercase()) {
                                    "pickup" -> StopType.PICKUP
                                    "drop" -> StopType.DROP
                                    else -> StopType.UNKNOWN
                                },
                                locationName = stopDto.locationName,
                                latitude = stopDto.coords.coordinates.getOrNull(1) ?: 0.0,
                                longitude = stopDto.coords.coordinates.getOrNull(0) ?: 0.0
                            )
                        }.sortedBy { it.sequence }
                    )
                }
                Result.success(trips)
            } else {
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch trips"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
