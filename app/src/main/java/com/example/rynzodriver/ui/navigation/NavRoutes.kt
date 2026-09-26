package com.example.rynzodriver.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String) {
    object Intro : Screen("intro")
    object Login : Screen("login")
    object MainContainer : Screen("main_container")
    object TripRequests : Screen("trip_requests")
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
