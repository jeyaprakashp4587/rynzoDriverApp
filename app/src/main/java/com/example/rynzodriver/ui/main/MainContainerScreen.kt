package com.example.rynzodriver.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.example.rynzodriver.ui.companyTrips.CompanyTripDetailScreen
import com.example.rynzodriver.ui.home.HomeScreen
import com.example.rynzodriver.ui.navigation.BottomBarScreen
import com.example.rynzodriver.ui.navigation.Screen
import com.example.rynzodriver.ui.profile.ProfileScreen
import com.example.rynzodriver.ui.trips.OrdersScreen
import com.example.rynzodriver.ui.trips.RequestedTripDetailScreen
import com.example.rynzodriver.ui.trips.TripRequestsScreen

@Composable
fun MainContainerScreen() {
    val navController = rememberNavController()
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Orders,
        BottomBarScreen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 0.dp
            ) {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        label = { Text(screen.title) },
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.primary,
                            selectedTextColor = MaterialTheme.colorScheme.primary,
                            unselectedIconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.12f)
                        ),
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = BottomBarScreen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(BottomBarScreen.Home.route) {
                HomeScreen(navController = navController)
            }
            composable(BottomBarScreen.Orders.route) { OrdersScreen() }
            composable(BottomBarScreen.Profile.route) { ProfileScreen() }
            composable(Screen.TripRequests.route) {
                TripRequestsScreen(navController = navController)
            }
            composable(
                route = Screen.TripDetail.route,
                arguments = listOf(navArgument("tripId") { type = NavType.StringType })
            ) { backStackEntry ->
                val tripId = backStackEntry.arguments?.getString("tripId") ?: return@composable

                RequestedTripDetailScreen(
                    tripId = tripId,
                    onDismiss = { navController.popBackStack() },
                    onApprove = { },
                    onReject = { }
                )
            }
            composable(
                route = Screen.CompanyTripDetail.route,
                arguments = listOf(
                    navArgument("routeId") { type = NavType.StringType },
                    navArgument("vehicleId") { type = NavType.StringType },
                    navArgument("driverId") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                    navArgument("title") { type = NavType.StringType },
                    navArgument("vehicleLabel") { type = NavType.StringType },
                    navArgument("stops") { type = NavType.StringType }
                )
            ) { backStackEntry ->
                val routeId = backStackEntry.arguments?.getString("routeId") ?: return@composable
                val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: return@composable
                val driverId = backStackEntry.arguments?.getString("driverId") ?: ""
                val title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "Company trip")
                val vehicleLabel = Uri.decode(backStackEntry.arguments?.getString("vehicleLabel") ?: "Vehicle")
                val stopsRaw = Uri.decode(backStackEntry.arguments?.getString("stops") ?: "")
                val stops = if (stopsRaw.isBlank()) emptyList() else stopsRaw.split("||").filter { it.isNotBlank() }

                CompanyTripDetailScreen(
                    routeId = routeId,
                    vehicleId = vehicleId,
                    driverId = driverId,
                    title = title,
                    vehicleLabel = vehicleLabel,
                    stops = stops,
                    onBack = { navController.popBackStack() }
                )
            }
        }
    }
}
