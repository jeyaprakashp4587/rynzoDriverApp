package com.example.rynzodriver.data.dto.auth

import com.google.gson.annotations.SerializedName

data class LoginRequest(
    @SerializedName("MobileNumber")
    val mobileNumber: String,
    @SerializedName("password")
    val password: String
)
