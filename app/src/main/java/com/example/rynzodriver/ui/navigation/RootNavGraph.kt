package com.example.rynzodriver.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rynzodriver.ui.intro.IntroScreen
import com.example.rynzodriver.ui.login.LoginScreen
import com.example.rynzodriver.ui.main.AuthState
import com.example.rynzodriver.ui.main.MainContainerScreen
import com.example.rynzodriver.ui.main.MainViewModel
import com.example.rynzodriver.ui.trips.RequestedTripDetailScreen
import com.example.rynzodriver.ui.trips.TripRequestsScreen

@Composable
fun RootNavGraph(
    viewModel: MainViewModel = hiltViewModel()
) {
    val authState by viewModel.authState.collectAsState()
    val navController = rememberNavController()

    if (authState is AuthState.Loading) {
        return
    }

    val startDestination = if (authState is AuthState.Authenticated) {
        Screen.MainContainer.route
    } else {
        Screen.Intro.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable(Screen.Intro.route) {
            IntroScreen(onContinue = {
                navController.navigate(Screen.Login.route)
            })
        }
        composable(Screen.Login.route) {
            LoginScreen(onLoginSuccess = {
                navController.navigate(Screen.MainContainer.route) {
                    popUpTo(Screen.Intro.route) { inclusive = true }
                }
            })
        }
        composable(Screen.MainContainer.route) {
            MainContainerScreen(
                onTripRequestsClick = {
                    navController.navigate(Screen.TripRequests.route)
                },
                onTripDetailClick = { tripId ->
                    navController.navigate(Screen.TripDetail.createRoute(tripId))
                }
            )
        }
        composable(Screen.TripRequests.route) {
            TripRequestsScreen(
                onViewTripDetail = { tripId ->
                    navController.navigate(Screen.TripDetail.createRoute(tripId))
                }
            )
        }
        composable(
            route = Screen.TripDetail.route,
            arguments = listOf(navArgument("tripId") { type = NavType.StringType })
        ) { backStackEntry ->
            val tripId = backStackEntry.arguments?.getString("tripId") ?: return@composable
            val tripViewModel: com.example.rynzodriver.ui.trips.TripRequestsViewModel = hiltViewModel()
            val uiState by tripViewModel.uiState.collectAsState()

            LaunchedEffect(tripId) {
                tripViewModel.fetchTripDetails(tripId)
            }

            RequestedTripDetailScreen(
                tripDetail = uiState.selectedTripDetail,
                isLoading = uiState.isDetailLoading,
                error = uiState.detailError,
                onDismiss = {
                    tripViewModel.dismissDetailSheet()
                    navController.popBackStack()
                },
                onApprove = { },
                onReject = { }
            )
        }
    }
}
