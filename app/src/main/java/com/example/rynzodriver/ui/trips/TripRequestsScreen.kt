package com.example.rynzodriver.ui.trips

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.rynzodriver.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TripRequestsScreen(
    navController: NavController,
    viewModel: TripRequestsViewModel = hiltViewModel()
) {
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
            if (uiState.isLoading) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            } else if (uiState.error != null) {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
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
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp)
                ) {
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
