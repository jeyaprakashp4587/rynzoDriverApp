package com.example.rynzodriver.ui.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rynzodriver.ui.permissions.PermissionHandler
import com.example.rynzodriver.ui.trips.RequestedTripsSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onViewRequestsClick: () -> Unit = {},
    onViewTripClick: (String) -> Unit = {}
) {
    PermissionHandler()

    val uiState by viewModel.uiState.collectAsState()
    val (isOnDuty, setOnDuty) = remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val currentLocationName = "Unknown Location"
    val selectedVehicle = uiState.vehicles.firstOrNull { it.id == uiState.selectedVehicleId }
        ?: uiState.vehicles.firstOrNull()

    val selectedVehicleName = selectedVehicle?.let { vehicle ->
        listOfNotNull(vehicle.vehicleModel, vehicle.vehicleNumber).firstOrNull { it.isNotBlank() }
            ?: "Vehicle ${vehicle.id.take(4)}"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopSection(
            isOnDuty = isOnDuty,
            onDutyChange = { setOnDuty(it) },
            userName = "John Doe",
            locationName = if (isOnDuty) currentLocationName else null,
            selectedVehicleName = selectedVehicleName,
            onSelectVehicleClick = {
                viewModel.fetchDriverVehicles()
                viewModel.openVehicleSheet()
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        RequestedTripsSection(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onViewClick = { trip -> onViewTripClick(trip.id) }
        )
    }

    if (uiState.isVehicleSheetOpen) {
        ModalBottomSheet(
            onDismissRequest = { viewModel.dismissVehicleSheet() },
            sheetState = bottomSheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Select vehicle",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                uiState.vehicles.ifEmpty {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 20.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No vehicle found for this driver")
                    }
                }

                uiState.vehicles.forEach { vehicle ->
                    val vehicleLabel = listOfNotNull(
                        vehicle.vehicleModel,
                        vehicle.vehicleNumber
                    ).firstOrNull { it.isNotBlank() }
                        ?: "Vehicle ${vehicle.id.take(4)}"

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { viewModel.selectVehicle(vehicle) },
                        color = if (uiState.selectedVehicleId == vehicle.id) {
                            MaterialTheme.colorScheme.primaryContainer
                        } else {
                            MaterialTheme.colorScheme.surface
                        },
                        tonalElevation = 0.dp,
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text(
                                    text = vehicleLabel,
                                    fontWeight = FontWeight.SemiBold
                                )
                                vehicle.vehicleNumber?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            if (uiState.selectedVehicleId == vehicle.id) {
                                Text(
                                    text = "Selected",
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
