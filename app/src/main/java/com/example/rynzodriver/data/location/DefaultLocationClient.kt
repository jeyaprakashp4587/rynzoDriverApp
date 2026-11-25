package com.example.rynzodriver.data.location

// Senior explanation:
// Android location updates, foreground tracking, and location state.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file manages Android location updates and location tracking state.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.os.Looper
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.IntentSenderRequest
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.util.hasLocationPermission
// Google Play Services imports: fused location providers, callbacks, and map coordinates.
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch

// Function `Context.requestLocationSettingsResolution`: normal synchronous execution is used unless the body starts a coroutine itself.
fun Context.requestLocationSettingsResolution(
    launcher: ActivityResultLauncher<IntentSenderRequest>
) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 10_000L)
        .setMinUpdateDistanceMeters(5f)
        .build()

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val settingsRequest = LocationSettingsRequest.Builder()
        .addLocationRequest(locationRequest)
        .setAlwaysShow(true)
        .build()

    LocationServices.getSettingsClient(this)
        .checkLocationSettings(settingsRequest)
        .addOnSuccessListener {
            // Location already enabled
        }
        .addOnFailureListener { exception ->
// `if` guard: the following block executes only while `exception is ResolvableApiException` evaluates to `true`; a false result skips it.
            if (exception is ResolvableApiException) {
                launcher.launch(IntentSenderRequest.Builder(exception.resolution).build())
            }
        }
}

// Runtime type: `DefaultLocationClient` owns the properties and methods declared in this class body.
class DefaultLocationClient(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val context: Context,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val client: FusedLocationProviderClient
): LocationClient {

// Lint directive: Android lint suppression is intentional for this framework callback/API.
    @SuppressLint("MissingPermission")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `getLocationUpdates`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
// Asynchronous stream: `Flow` represents a sequence of values delivered over time rather than one immediate result.
    override fun getLocationUpdates(interval: Long): Flow<Location> {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return callbackFlow {
// `if` guard: the following block executes only while `!context.hasLocationPermission()` evaluates to `true`; a false result skips it.
            if(!context.hasLocationPermission()) {
// `throw` failure: this explicitly creates an exceptional exit and transfers control to the nearest compatible handler.
                throw LocationClient.LocationException("Missing location permission")
            }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
// `if` guard: the following block executes only while `!isGpsEnabled && !isNetworkEnabled` evaluates to `true`; a false result skips it.
            if(!isGpsEnabled && !isNetworkEnabled) {
// `throw` failure: this explicitly creates an exceptional exit and transfers control to the nearest compatible handler.
                throw LocationClient.LocationException("GPS is disabled")
            }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, interval)
                .setMinUpdateDistanceMeters(5f) 
                .build()

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val locationCallback = object : LocationCallback() {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `onLocationResult`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
                override fun onLocationResult(result: LocationResult) {
                    super.onLocationResult(result)
// Safe-call chain: `?.let` runs the block only when the receiver is non-null; inside the block `it` is the non-null value.
                    result.locations.lastOrNull()?.let { location ->
                        launch { send(location) }
                    }
                }
            }

            client.requestLocationUpdates(
                request,
                locationCallback,
                Looper.getMainLooper()
            )

            awaitClose {
                client.removeLocationUpdates(locationCallback)
            }
        }
    }
}
