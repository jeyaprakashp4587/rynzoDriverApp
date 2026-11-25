package com.example.rynzodriver.data.local.db.entity

// Senior explanation:
// Local authentication preferences and Room database persistence.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements local persistence for authentication or database records.

// Room imports: SQLite persistence entities, database access, and query definitions.
import androidx.room.Entity
import androidx.room.PrimaryKey

// Room mapping: this data class is persisted as a table/entity in SQLite.
@Entity(tableName = "locations")
// Data model: `LocationEntity` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class LocationEntity(
// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
    @PrimaryKey(autoGenerate = true)
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val id: Long = 0L,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val latitude: Double,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val longitude: Double,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val timestamp: Long,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val tripId: String? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val driverId: String? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val userId: String? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val synced: Boolean = false
)
