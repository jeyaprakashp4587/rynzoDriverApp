package com.example.rynzodriver.di

// Senior explanation:
// Hilt dependency providers and interface bindings.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file configures Hilt dependency injection.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.content.Context
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.location.DefaultLocationClient
import com.example.rynzodriver.data.location.LocationClient
// Google Play Services imports: fused location providers, callbacks, and map coordinates.
import com.google.android.gms.location.LocationServices
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
// Singleton object: `LocationModule` has one process-wide instance; initialization occurs the first time it is accessed.
object LocationModule {

// Provider method: Hilt calls this method to create the declared dependency type.
    @Provides
// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
    @Singleton
// Function `provideLocationClient`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun provideLocationClient(
// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
        @ApplicationContext context: Context
    ): LocationClient {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return DefaultLocationClient(
            context,
            LocationServices.getFusedLocationProviderClient(context)
        )
    }
}
