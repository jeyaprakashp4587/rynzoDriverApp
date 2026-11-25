package com.example.rynzodriver.data.local.db.dao

// Senior explanation:
// Local authentication preferences and Room database persistence.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements local persistence for authentication or database records.

// Room imports: SQLite persistence entities, database access, and query definitions.
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.local.db.entity.LocationEntity
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import kotlinx.coroutines.flow.Flow

// Room DAO: this interface translates annotated methods into database operations.
@Dao
// Contract: `LocationDao` declares capabilities. Concrete classes implement these methods without changing callers.
interface LocationDao {

// Database operation: Room inserts entity values and applies the declared conflict strategy.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
// Function `insertLocation`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun insertLocation(location: LocationEntity): Long

// Database operation: Room inserts entity values and applies the declared conflict strategy.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
// Function `insertLocations`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun insertLocations(locations: List<LocationEntity>)

// Database operation: Room executes this SQL against the matching entity table.
    @Query("SELECT * FROM locations ORDER BY timestamp DESC")
// Function `getAllLocations`: normal synchronous execution is used unless the body starts a coroutine itself.
// Asynchronous stream: `Flow` represents a sequence of values delivered over time rather than one immediate result.
    fun getAllLocations(): Flow<List<LocationEntity>>

// Database operation: Room executes this SQL against the matching entity table.
    @Query("SELECT * FROM locations WHERE synced = 0 ORDER BY timestamp ASC")
// Function `getUnsyncedLocations`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun getUnsyncedLocations(): List<LocationEntity>

// Database operation: Room executes this SQL against the matching entity table.
    @Query("UPDATE locations SET synced = 1 WHERE id IN (:ids)")
// Function `markAsSynced`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun markAsSynced(ids: List<Long>)

// Database operation: Room executes this SQL against the matching entity table.
    @Query("DELETE FROM locations WHERE synced = 1")
// Function `deleteSyncedLocations`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun deleteSyncedLocations()

// Database operation: Room executes this SQL against the matching entity table.
    @Query("DELETE FROM locations")
// Function `clearAll`: `suspend` means it may pause without blocking the thread and must be called from a coroutine or another suspend function.
    suspend fun clearAll()
}
