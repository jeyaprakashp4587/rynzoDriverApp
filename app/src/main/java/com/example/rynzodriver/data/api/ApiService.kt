package com.example.rynzodriver.data.api

import com.example.rynzodriver.data.dto.company.CompanyRunStartRequest
import com.example.rynzodriver.data.dto.company.CompanyRunStartResponse
import com.example.rynzodriver.data.dto.company.CompanyTripListResponse
import com.example.rynzodriver.data.dto.vehicles.DriverProfileResponse
import com.example.rynzodriver.data.dto.vehicles.DriverVehicleListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("api/drivers/me")
    suspend fun getDriverProfile(): Response<DriverProfileResponse>

    @GET("api/vehicles/driver/list")
    suspend fun getDriverVehicles(
        @Query("userId") userId: String
    ): Response<DriverVehicleListResponse>

    @GET("api/trips/company/driver/routes")
    suspend fun getDriverCompanyRoutes(
        @Query("userId") userId: String,
        @Query("vehicleId") vehicleId: String
    ): Response<CompanyTripListResponse>

    @POST("api/trips/company/routes/{routeId}/runs")
    suspend fun startCompanyRun(
        @Path("routeId") routeId: String,
        @Body request: CompanyRunStartRequest
    ): Response<CompanyRunStartResponse>
}
