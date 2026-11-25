package com.example.rynzodriver.data.local.db

// Senior explanation:
// Local authentication preferences and Room database persistence.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements local persistence for authentication or database records.

// Room imports: SQLite persistence entities, database access, and query definitions.
import androidx.room.Database
import androidx.room.RoomDatabase
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.local.db.dao.LocationDao
import com.example.rynzodriver.data.local.db.entity.LocationEntity

// Room database: this abstract type registers entities and exposes DAO access.
@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
// Abstract base: `AppDatabase` can require implementations while sharing lifecycle or utility behavior.
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}
