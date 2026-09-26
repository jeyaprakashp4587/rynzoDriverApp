package com.example.rynzodriver.domain.model

data class User(
    val id: String,
    val name: String,
    val mobileNumber: String,
    val role: String,
    val token: String
)
