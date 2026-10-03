package com.example.rynzodriver.data.api

import com.example.rynzodriver.data.dto.vehicles.DriverVehicleListResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/vehicles/driver/list")
    suspend fun getDriverVehicles(
        @Query("userId") userId: String
    ): Response<DriverVehicleListResponse>
}
