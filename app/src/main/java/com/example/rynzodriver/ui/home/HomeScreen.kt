package com.example.rynzodriver.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rynzodriver.ui.orders.RequestedTripDetailScreen
import com.example.rynzodriver.ui.orders.RequestedTripsCard
import com.example.rynzodriver.ui.permissions.PermissionHandler
import com.example.rynzodriver.ui.trips.TripRequestsViewModel

@Composable
fun HomeScreen(
    onViewRequestsClick: () -> Unit = {},
    viewModel: TripRequestsViewModel = hiltViewModel()
) {
    PermissionHandler()
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Trip Requests",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator()
                }
                uiState.error != null -> {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = uiState.error!!, color = MaterialTheme.colorScheme.error)
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = { viewModel.fetchTripRequests() }) {
                            Text("Retry")
                        }
                    }
                }
                uiState.trips.isEmpty() -> {
                    Text(
                        text = "No trip requests available",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                else -> {
                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        contentPadding = PaddingValues(horizontal = 4.dp)
                    ) {
                        items(uiState.trips) { trip ->
                            RequestedTripsCard(
                                trip = trip,
                                onViewClick = {
                                    viewModel.fetchTripDetails(trip.id)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (uiState.selectedTripDetail != null || uiState.isDetailLoading || uiState.detailError != null) {
        RequestedTripDetailScreen(
            tripDetail = uiState.selectedTripDetail,
            isLoading = uiState.isDetailLoading,
            error = uiState.detailError,
            onDismiss = { viewModel.dismissDetailSheet() },
            onApprove = { },
            onReject = { }
        )
    }
}
