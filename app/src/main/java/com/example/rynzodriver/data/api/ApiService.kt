package com.example.rynzodriver.data.api

import com.example.rynzodriver.data.dto.LocationUpdateDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("driver/location")
    suspend fun updateLocation(@Body request: LocationUpdateDto): Response<Unit>
}
