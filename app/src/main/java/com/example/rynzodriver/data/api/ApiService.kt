package com.example.rynzodriver.data.api

// Senior explanation:
// Retrofit endpoint contracts and HTTP request interception.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file defines HTTP API contracts or request authentication behavior.

// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.dto.company.CompanyRunStartRequest
import com.example.rynzodriver.data.dto.company.CompanyRunStartResponse
import com.example.rynzodriver.data.dto.company.CompanyTripListResponse
import com.example.rynzodriver.data.dto.vehicles.ActiveTripResponse
import com.example.rynzodriver.data.dto.vehicles.DriverProfileResponse
import com.example.rynzodriver.data.dto.vehicles.DriverVehicleListResponse
// Retrofit imports: HTTP endpoint declarations and response handling.
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

// Contract: `ApiService` declares capabilities. Concrete classes implement these methods without changing callers.
interface ApiService {
// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
    @GET("api/drivers/me")
// Function `getDriverProfile`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun getDriverProfile(): Response<DriverProfileResponse>

// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
    @GET("api/drivers/activeTrip")
// Function `getDriverActiveTrip`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun getDriverActiveTrip(): Response<ActiveTripResponse>

// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
    @GET("api/vehicles/driver/list")
// Function `getDriverVehicles`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun getDriverVehicles(
// Database operation: Room executes this SQL against the matching entity table.
        @Query("userId") userId: String
    ): Response<DriverVehicleListResponse>

// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
    @GET("api/trips/company/driver/routes")
// Function `getDriverCompanyRoutes`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun getDriverCompanyRoutes(
// Database operation: Room executes this SQL against the matching entity table.
        @Query("userId") userId: String,
// Database operation: Room executes this SQL against the matching entity table.
        @Query("vehicleId") vehicleId: String
    ): Response<CompanyTripListResponse>
// update
// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
    @POST("api/trips/company/routes/{routeId}/runs")
// Function `startCompanyRun`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun startCompanyRun(
// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
        @Path("routeId") routeId: String,
// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
        @Body request: CompanyRunStartRequest
    ): Response<CompanyRunStartResponse>
}
