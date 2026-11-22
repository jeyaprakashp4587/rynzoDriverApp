package com.example.rynzodriver.ui.navigation

import android.net.Uri
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.rynzodriver.ui.companyTrips.CompanyTripUiModel

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Login : Screen("login")
    object MainContainer : Screen("main_container")
    object TripRequests : Screen("trip_requests")
    object TripDetail : Screen("trip_detail/{tripId}") {
        fun createRoute(tripId: String) = "trip_detail/$tripId"
    }
    object CompanyTripDetail : Screen("company_trip_detail/{routeId}/{vehicleId}/{driverId}/{title}/{vehicleLabel}/{stops}") {
        fun createRoute(trip: CompanyTripUiModel): String {
            val title = Uri.encode(trip.title.ifBlank { "Company trip" })
            val vehicleLabel = Uri.encode(trip.vehicleLabel.ifBlank { "Vehicle" })
            val stops = Uri.encode(trip.stops.joinToString("||"))
            val safeDriverId = if (trip.driverId.isBlank()) "none" else trip.driverId
            return "company_trip_detail/${trip.routeId}/${trip.vehicleId}/$safeDriverId/$title/$vehicleLabel/$stops"
        }

        fun createRoute(
            routeId: String,
            vehicleId: String,
            driverId: String = "none",
            title: String = "Company trip",
            vehicleLabel: String = "Vehicle",
            stops: List<String> = emptyList()
        ): String {
            val safeDriverId = if (driverId.isBlank()) "none" else driverId
            val encodedTitle = Uri.encode(title.ifBlank { "Company trip" })
            val encodedVehicleLabel = Uri.encode(vehicleLabel.ifBlank { "Vehicle" })
            val encodedStops = Uri.encode(stops.joinToString("||"))
            return "company_trip_detail/$routeId/$vehicleId/$safeDriverId/$encodedTitle/$encodedVehicleLabel/$encodedStops"
        }
    }
}

sealed class BottomBarScreen(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarScreen("home", "Home", Icons.Default.Home)
    object Orders : BottomBarScreen("orders", "Orders", Icons.Default.List)
    object Profile : BottomBarScreen("profile", "Profile", Icons.Default.Person)
}
