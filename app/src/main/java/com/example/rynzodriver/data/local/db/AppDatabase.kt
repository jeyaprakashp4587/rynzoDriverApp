package com.example.rynzodriver.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.rynzodriver.data.local.db.dao.LocationDao
import com.example.rynzodriver.data.local.db.entity.LocationEntity

@Database(entities = [LocationEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun locationDao(): LocationDao
}
