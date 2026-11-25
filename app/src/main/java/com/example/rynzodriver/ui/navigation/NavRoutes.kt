package com.example.rynzodriver.ui.navigation

// Senior explanation:
// Navigation routes, route argument encoding, bottom destinations, and graph setup.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file defines application navigation routes and navigation behavior.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.net.Uri
// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.ui.companyTrips.CompanyTripUiModel

// Restricted type hierarchy: `{` only permits implementations/types listed in this module, enabling exhaustive `when` handling.
sealed class Screen(val route: String) {
// Singleton object: `Intro` has one process-wide instance; initialization occurs the first time it is accessed.
    object Intro : Screen("intro")
// Singleton object: `Login` has one process-wide instance; initialization occurs the first time it is accessed.
    object Login : Screen("login")
// Singleton object: `MainContainer` has one process-wide instance; initialization occurs the first time it is accessed.
    object MainContainer : Screen("main_container")
// Singleton object: `TripRequests` has one process-wide instance; initialization occurs the first time it is accessed.
    object TripRequests : Screen("trip_requests")
// Singleton object: `TripDetail` has one process-wide instance; initialization occurs the first time it is accessed.
    object TripDetail : Screen("trip_detail/{tripId}") {
// Function `createRoute`: normal synchronous execution is used unless the body starts a coroutine itself.
        fun createRoute(tripId: String) = "trip_detail/$tripId"
    }
// Singleton object: `CompanyTripDetail` has one process-wide instance; initialization occurs the first time it is accessed.
    object CompanyTripDetail : Screen("company_trip_detail/{routeId}/{vehicleId}/{driverId}/{title}/{vehicleLabel}/{stops}") {
// Function `createRoute`: normal synchronous execution is used unless the body starts a coroutine itself.
        fun createRoute(trip: CompanyTripUiModel): String {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val title = Uri.encode(trip.title.ifBlank { "Company trip" })
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val vehicleLabel = Uri.encode(trip.vehicleLabel.ifBlank { "Vehicle" })
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val stops = Uri.encode(trip.stops.joinToString("||"))
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val safeDriverId = if (trip.driverId.isBlank()) "none" else trip.driverId
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
            return "company_trip_detail/${trip.routeId}/${trip.vehicleId}/$safeDriverId/$title/$vehicleLabel/$stops"
        }

// Function `createRoute`: normal synchronous execution is used unless the body starts a coroutine itself.
        fun createRoute(
            routeId: String,
            vehicleId: String,
            driverId: String = "none",
            title: String = "Company trip",
            vehicleLabel: String = "Vehicle",
            stops: List<String> = emptyList()
        ): String {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val safeDriverId = if (driverId.isBlank()) "none" else driverId
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val encodedTitle = Uri.encode(title.ifBlank { "Company trip" })
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val encodedVehicleLabel = Uri.encode(vehicleLabel.ifBlank { "Vehicle" })
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val encodedStops = Uri.encode(stops.joinToString("||"))
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
            return "company_trip_detail/$routeId/$vehicleId/$safeDriverId/$encodedTitle/$encodedVehicleLabel/$encodedStops"
        }
    }
}

// Restricted type hierarchy: `BottomBarScreen` only permits implementations/types listed in this module, enabling exhaustive `when` handling.
sealed class BottomBarScreen(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val route: String,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val title: String,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val icon: ImageVector
) {
// Singleton object: `Home` has one process-wide instance; initialization occurs the first time it is accessed.
    object Home : BottomBarScreen("home", "Home", Icons.Default.Home)
// Singleton object: `Orders` has one process-wide instance; initialization occurs the first time it is accessed.
    object Orders : BottomBarScreen("orders", "Orders", Icons.Default.List)
// Singleton object: `Profile` has one process-wide instance; initialization occurs the first time it is accessed.
    object Profile : BottomBarScreen("profile", "Profile", Icons.Default.Person)
}
