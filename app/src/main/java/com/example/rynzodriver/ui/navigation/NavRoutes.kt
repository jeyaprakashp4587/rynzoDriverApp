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
            val title = Uri.encode(trip.title)
            val vehicleLabel = Uri.encode(trip.vehicleLabel)
            val stops = Uri.encode(trip.stops.joinToString("||"))
            return "company_trip_detail/${trip.routeId}/${trip.vehicleId}/${trip.driverId}/$title/$vehicleLabel/$stops"
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
