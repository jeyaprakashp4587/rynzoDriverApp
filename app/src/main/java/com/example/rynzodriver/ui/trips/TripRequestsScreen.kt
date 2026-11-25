package com.example.rynzodriver.ui.trips

// Senior explanation:
// Requested-trip presentation, list/detail state, and trip acceptance flow.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements trip requests, trip details, and their Compose UI.

// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
// Hilt imports: dependency injection annotations and ViewModel integration.
import androidx.hilt.navigation.compose.hiltViewModel
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// AndroidX imports: navigation or lifecycle infrastructure used by this file.
import androidx.navigation.NavController
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.ui.navigation.Screen

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Opt-in boundary: this declaration intentionally uses an API marked experimental by the library.
@OptIn(ExperimentalMaterial3Api::class)
// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `TripRequestsScreen`: normal synchronous execution is used unless the body starts a coroutine itself.
fun TripRequestsScreen(
    navController: NavController,
// Default dependency: if the caller omits this parameter, Hilt creates or retrieves the ViewModel for this Compose destination.
    viewModel: TripRequestsViewModel = hiltViewModel()
) {
// Delegated property: `by` delegates reading/writing to the expression to the right. The delegate owns storage and observation semantics.
// State bridge: `collectAsState()` observes the Flow and exposes its latest value as Compose state, so the UI recomposes on each emission.
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Trip Requests") }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
// `if` guard: the following block executes only while `uiState.isLoading` evaluates to `true`; a false result skips it.
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
// Explicit non-null assertion: `!!` tells Kotlin the value cannot be null; null at runtime throws an exception instead.
                    Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
                    Button(onClick = { viewModel.fetchTripRequests() }) {
                        Text("Retry")
                    }
                }
            } else if (uiState.trips.isEmpty()) {
                Text(
                    text = "No trip requests available",
                    modifier = Modifier.align(Alignment.Center),
                    style = MaterialTheme.typography.bodyLarge
                )
            } else {
                LazyColumn(
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Available trips",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )
                    }

                    items(uiState.trips) { trip ->
                        RequestedTripsCard(
                            trip = trip,
                            onViewClick = {
                                navController.navigate(Screen.TripDetail.createRoute(trip.id))
                            }
                        )
                    }
                }
            }
        }
    }
}
