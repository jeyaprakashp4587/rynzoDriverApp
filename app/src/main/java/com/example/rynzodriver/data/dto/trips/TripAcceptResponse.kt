package com.example.rynzodriver.data.dto.trips

import com.google.gson.annotations.SerializedName

data class TripAcceptResponse(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: Any? = null
)
