package com.example.rynzodriver.ui.home

import android.content.Context
import android.content.Intent
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.rynzodriver.data.location.LocationService
import com.example.rynzodriver.data.location.LocationTrackingState
import com.example.rynzodriver.data.location.requestLocationSettingsResolution
import com.example.rynzodriver.ui.navigation.Screen
import com.example.rynzodriver.ui.permissions.PermissionHandler
import com.example.rynzodriver.ui.trips.RequestedTripsSection
import com.example.rynzodriver.util.hasLocationPermission

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    PermissionHandler()

    val context = LocalContext.current
    val uiState by viewModel.uiState.collectAsState()
    val locationState by LocationTrackingState.state.collectAsState()
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { }

    LaunchedEffect(uiState.isOnDuty) {
        if (uiState.isOnDuty) {
            if (!context.hasLocationPermission()) {
                viewModel.setOnDuty(false)
                stopLocationTracking(context)
                return@LaunchedEffect
            }

            val locationManager = context.getSystemService(LocationManager::class.java)
            val gpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
            val networkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
            if (!gpsEnabled && !networkEnabled) {
                viewModel.setOnDuty(false)
                stopLocationTracking(context)
                context.requestLocationSettingsResolution(locationSettingsLauncher)
                return@LaunchedEffect
            }
            startLocationTracking(context)
        } else {
            stopLocationTracking(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopSection(
            viewModel = viewModel,
            currentLocation = locationState.currentLocation,
            lastLocation = locationState.lastLocation,
            userName = "John Doe",
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
            onViewClick = { trip ->
                navController.navigate(Screen.TripDetail.createRoute(trip.id))
            }
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

private fun startLocationTracking(context: Context) {
    val intent = Intent(context, LocationService::class.java)
    intent.action = LocationService.ACTION_START
    context.startService(intent)
}

private fun stopLocationTracking(context: Context) {
    val intent = Intent(context, LocationService::class.java)
    intent.action = LocationService.ACTION_STOP
    context.startService(intent)
}


