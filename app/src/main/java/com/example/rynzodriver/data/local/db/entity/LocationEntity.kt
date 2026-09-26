package com.example.rynzodriver.data.local.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locations")
data class LocationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val latitude: Double,
    val longitude: Double,
    val timestamp: Long,
    val tripId: String? = null,
    val driverId: String? = null,
    val userId: String? = null,
    val synced: Boolean = false
)
