package com.example.rynzodriver.di

// Senior explanation:
// Hilt dependency providers and interface bindings.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file configures Hilt dependency injection.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.content.Context
// Room imports: SQLite persistence entities, database access, and query definitions.
import androidx.room.Room
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.local.db.AppDatabase
import com.example.rynzodriver.data.local.db.dao.LocationDao
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import dagger.Module
import dagger.Provides
// Hilt imports: dependency injection annotations and ViewModel integration.
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import javax.inject.Singleton

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Dependency module: Hilt reads the providers or bindings declared by this class/object.
@Module
// Scope binding: these dependencies are installed into the specified Hilt component lifecycle.
@InstallIn(SingletonComponent::class)
// Singleton object: `DatabaseModule` has one process-wide instance; initialization occurs the first time it is accessed.
object DatabaseModule {

// Provider method: Hilt calls this method to create the declared dependency type.
    @Provides
// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
    @Singleton
// Function `provideAppDatabase`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "rynzodriver_database"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

// Provider method: Hilt calls this method to create the declared dependency type.
    @Provides
// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
    @Singleton
// Function `provideLocationDao`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun provideLocationDao(database: AppDatabase): LocationDao {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return database.locationDao()
    }
}
