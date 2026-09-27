package com.example.rynzodriver.ui.main

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.example.rynzodriver.ui.home.HomeScreen
import com.example.rynzodriver.ui.navigation.BottomBarScreen
import com.example.rynzodriver.ui.orders.OrdersScreen
import com.example.rynzodriver.ui.profile.ProfileScreen


@Composable
fun MainContainerScreen(onTripRequestsClick: () -> Unit) {
    val navController = rememberNavController()
    val screens = listOf(
        BottomBarScreen.Home,
        BottomBarScreen.Orders,
        BottomBarScreen.Profile
    )

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination

                screens.forEach { screen ->
                    NavigationBarItem(
                        label = { Text(screen.title) },
                        icon = { Icon(screen.icon, contentDescription = screen.title) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
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
                HomeScreen(onViewRequestsClick = onTripRequestsClick) 
            }
            composable(BottomBarScreen.Orders.route) { OrdersScreen() }
            composable(BottomBarScreen.Profile.route) { ProfileScreen() }
        }
    }
}
