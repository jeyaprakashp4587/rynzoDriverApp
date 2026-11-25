package com.example.rynzodriver.data.repository.trips

// Senior explanation:
// Repository implementations that bridge APIs/services into domain contracts.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements domain repository contracts using data sources.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.api.trips.TripApiService
import com.example.rynzodriver.domain.model.trips.Stop
import com.example.rynzodriver.domain.model.trips.StopType
import com.example.rynzodriver.domain.model.trips.Trip
import com.example.rynzodriver.domain.model.trips.TripDetail
import com.example.rynzodriver.domain.repository.trips.TripRepository
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import javax.inject.Inject

// Runtime type: `TripRepositoryImpl` owns the properties and methods declared in this class body.
class TripRepositoryImpl @Inject constructor(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val tripApiService: TripApiService
) : TripRepository {

    override suspend fun getTripRequests(): Result<List<Trip>> {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Exception boundary: operations here may throw; success continues normally and matching `catch` blocks handle failures.
        return try {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val response = tripApiService.getRequestTrips()
// `if` guard: the following block executes only while `response.isSuccessful && response.body() != null` evaluates to `true`; a false result skips it.
            if (response.isSuccessful && response.body() != null) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Explicit non-null assertion: `!!` tells Kotlin the value cannot be null; null at runtime throws an exception instead.
                val tripDtos = response.body()!!.data
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Collection transformation: `map` returns a new list with one transformed result per item; `mapIndexed` also supplies each index.
                val trips = tripDtos.map { dto ->
                    Trip(
                        id = dto.id,
                        status = dto.status,
                        createdAt = dto.createdAt,
                        tripType = dto.tripType,
                        tripMode = dto.tripMode,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                        customerName = dto.createdBy?.name ?: "Unknown Customer",
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                        customerPhone = dto.createdBy?.mobileNumber ?: "",
// Collection transformation: `map` returns a new list with one transformed result per item; `mapIndexed` also supplies each index.
                        stops = dto.stops.stops.map { stopDto ->
                            Stop(
                                sequence = stopDto.sequence,
// Branch expression: `when` is an expression and selects the first branch whose condition matches the current state.
                                type = when (stopDto.stopType?.lowercase()) {
                                    "pickup" -> StopType.PICKUP
                                    "drop" -> StopType.DROP
// `else` fallback: this block runs only when the preceding `if` condition did not match.
                                    else -> StopType.UNKNOWN
                                },
                                locationName = stopDto.locationName,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                                latitude = stopDto.coords.coordinates.getOrNull(1) ?: 0.0,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                                longitude = stopDto.coords.coordinates.getOrNull(0) ?: 0.0
                            )
                        }.sortedBy { it.sequence }
                    )
                }
// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
                Result.success(trips)
            } else {
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch trips"))
            }
// Failure recovery: this catch block receives the thrown exception and can update state, notify the UI, or rethrow.
        } catch (e: Exception) {
// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
            Result.failure(e)
        }
    }

    override suspend fun getTripDetails(tripId: String): Result<TripDetail> {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Exception boundary: operations here may throw; success continues normally and matching `catch` blocks handle failures.
        return try {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val response = tripApiService.getParticularRequestedTripDetails(tripId)
// `if` guard: the following block executes only while `response.isSuccessful && response.body() != null` evaluates to `true`; a false result skips it.
            if (response.isSuccessful && response.body() != null) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Explicit non-null assertion: `!!` tells Kotlin the value cannot be null; null at runtime throws an exception instead.
                val data = response.body()!!.data
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val tripDetail = TripDetail(
                    id = data.id,
                    status = data.status,
                    tripType = data.tripType,
                    createdAt = data.createdAt,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                    customerName = data.users.firstOrNull()?.user?.name ?: "Customer",
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                    customerPhone = data.users.firstOrNull()?.driver?.mobileNumber ?: "",
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                    driverName = data.users.firstOrNull()?.driver?.name ?: "Driver",
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                    driverPhone = data.users.firstOrNull()?.driver?.mobileNumber ?: "",
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                    vehicleNumber = data.users.firstOrNull()?.vehicle?.vehicleNumber ?: "",
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                    vehicleModel = data.users.firstOrNull()?.vehicle?.vehicleModel ?: "",
// Collection transformation: `map` returns a new list with one transformed result per item; `mapIndexed` also supplies each index.
                    stops = data.stops.stops.map { stopDto ->
                        Stop(
                            sequence = stopDto.sequence,
// Branch expression: `when` is an expression and selects the first branch whose condition matches the current state.
                            type = when (stopDto.stopType?.lowercase()) {
                                "pickup" -> StopType.PICKUP
                                "drop" -> StopType.DROP
// `else` fallback: this block runs only when the preceding `if` condition did not match.
                                else -> StopType.UNKNOWN
                            },
                            locationName = stopDto.locationName,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                            latitude = stopDto.coords.coordinates.getOrNull(1) ?: 0.0,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                            longitude = stopDto.coords.coordinates.getOrNull(0) ?: 0.0
                        )
                    }.sortedBy { it.sequence }
                )
// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
                Result.success(tripDetail)
            } else {
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                Result.failure(Exception(response.body()?.message ?: "Failed to fetch trip details"))
            }
// Failure recovery: this catch block receives the thrown exception and can update state, notify the UI, or rethrow.
        } catch (e: Exception) {
// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
            Result.failure(e)
        }
    }

    override suspend fun acceptTripRequest(tripId: String): Result<String> {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Exception boundary: operations here may throw; success continues normally and matching `catch` blocks handle failures.
        return try {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val response = tripApiService.acceptTripRequest(tripId)
// `if` guard: the following block executes only while `response.isSuccessful` evaluates to `true`; a false result skips it.
            if (response.isSuccessful) {
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                Result.success(response.body()?.message ?: "Trip request accepted successfully")
            } else {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val errorMessage = response.errorBody()?.string()
                    ?: response.body()?.message
                    ?: "Failed to accept trip request"
// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
                Result.failure(Exception(errorMessage))
            }
// Failure recovery: this catch block receives the thrown exception and can update state, notify the UI, or rethrow.
        } catch (e: Exception) {
// Kotlin Result: this stores success or failure as a typed value instead of requiring an immediate thrown exception.
            Result.failure(e)
        }
    }
}
