package com.example.rynzodriver.ui.main

// Senior explanation:
// Authenticated application shell, root state, and bottom-navigation container.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements the main application shell and authentication state.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.net.Uri
// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// AndroidX imports: navigation or lifecycle infrastructure used by this file.
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.ui.companyTrips.CompanyTripDetailScreen
import com.example.rynzodriver.ui.home.HomeScreen
import com.example.rynzodriver.ui.navigation.BottomBarScreen
import com.example.rynzodriver.ui.navigation.Screen
import com.example.rynzodriver.ui.profile.ProfileScreen
import com.example.rynzodriver.ui.trips.OrdersScreen
import com.example.rynzodriver.ui.trips.RequestedTripDetailScreen
import com.example.rynzodriver.ui.trips.TripRequestsScreen

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `MainContainerScreen`: normal synchronous execution is used unless the body starts a coroutine itself.
fun MainContainerScreen() {
// Recomposition memory: `remember` retains this value through recompositions and recreates it only when the composition key changes.
    val navController = rememberNavController()
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
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
// Delegated property: `by` delegates reading/writing to the expression to the right. The delegate owns storage and observation semantics.
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
                val navBackStackEntry by navController.currentBackStackEntryAsState()
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val currentDestination = navBackStackEntry?.destination

// Collection iteration: `forEach` executes the lambda once for every item; the item is bound as `it` unless named explicitly.
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
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
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
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
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
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                val routeId = backStackEntry.arguments?.getString("routeId") ?: return@composable
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                val vehicleId = backStackEntry.arguments?.getString("vehicleId") ?: return@composable
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Safe-call chain: `?.let` runs the block only when the receiver is non-null; inside the block `it` is the non-null value.
                val driverId = backStackEntry.arguments?.getString("driverId")?.let { if (it == "none") "" else it } ?: ""
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                val title = Uri.decode(backStackEntry.arguments?.getString("title") ?: "Company trip")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                val vehicleLabel = Uri.decode(backStackEntry.arguments?.getString("vehicleLabel") ?: "Vehicle")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                val stopsRaw = Uri.decode(backStackEntry.arguments?.getString("stops") ?: "")
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
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
