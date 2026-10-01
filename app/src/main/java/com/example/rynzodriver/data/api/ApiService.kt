package com.example.rynzodriver.data.api

import com.example.rynzodriver.data.dto.LocationUpdateDto
import com.example.rynzodriver.data.dto.vehicles.DriverVehicleListResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @POST("driver/location")
    suspend fun updateLocation(@Body request: LocationUpdateDto): Response<Unit>

    @GET("api/vehicles/driver/list")
    suspend fun getDriverVehicles(
        @Query("userId") userId: String
    ): Response<DriverVehicleListResponse>
}
