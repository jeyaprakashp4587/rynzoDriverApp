package com.example.rynzodriver.ui.navigation

// Senior explanation:
// Navigation routes, route argument encoding, bottom destinations, and graph setup.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file defines application navigation routes and navigation behavior.

// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
// Hilt imports: dependency injection annotations and ViewModel integration.
import androidx.hilt.navigation.compose.hiltViewModel
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// AndroidX imports: navigation or lifecycle infrastructure used by this file.
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.ui.intro.IntroScreen
import com.example.rynzodriver.ui.login.LoginScreen
import com.example.rynzodriver.ui.main.AuthState
import com.example.rynzodriver.ui.main.MainContainerScreen
import com.example.rynzodriver.ui.main.MainViewModel
import com.example.rynzodriver.ui.trips.RequestedTripDetailScreen
import com.example.rynzodriver.ui.trips.TripRequestsScreen

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `RootNavGraph`: normal synchronous execution is used unless the body starts a coroutine itself.
fun RootNavGraph(
// Default dependency: if the caller omits this parameter, Hilt creates or retrieves the ViewModel for this Compose destination.
    viewModel: MainViewModel = hiltViewModel()
) {
// Delegated property: `by` delegates reading/writing to the expression to the right. The delegate owns storage and observation semantics.
// State bridge: `collectAsState()` observes the Flow and exposes its latest value as Compose state, so the UI recomposes on each emission.
    val authState by viewModel.authState.collectAsState()
// Recomposition memory: `remember` retains this value through recompositions and recreates it only when the composition key changes.
    val navController = rememberNavController()

// `if` guard: the following block executes only while `authState is AuthState.Loading` evaluates to `true`; a false result skips it.
    if (authState is AuthState.Loading) {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return
    }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val startDestination = if (authState is AuthState.Authenticated) {
        Screen.MainContainer.route
    } else {
        Screen.Intro.route
    }

// Side-effect lifecycle: this coroutine runs when the Composable enters or when any key in parentheses changes.
    LaunchedEffect(authState) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val currentRoute = navController.currentDestination?.route
// `when` branch selection: Kotlin evaluates conditions in source order and executes only the first matching branch.
// Branch expression: `when` is an expression and selects the first branch whose condition matches the current state.
        when (authState) {
            is AuthState.Authenticated -> {
// `if` guard: the following block executes only while `currentRoute != Screen.MainContainer.route` evaluates to `true`; a false result skips it.
                if (currentRoute != Screen.MainContainer.route) {
                    navController.navigate(Screen.MainContainer.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
            is AuthState.Unauthenticated -> {
// `if` guard: the following block executes only while `currentRoute != Screen.Intro.route` evaluates to `true`; a false result skips it.
                if (currentRoute != Screen.Intro.route) {
                    navController.navigate(Screen.Intro.route) {
                        popUpTo(0) { inclusive = true }
                        launchSingleTop = true
                    }
                }
            }
// `else` fallback: this block runs only when the preceding `if` condition did not match.
            else -> Unit
        }
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
            MainContainerScreen()
        }
    }
}
