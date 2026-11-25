package com.example.rynzodriver.ui.home

// Senior explanation:
// Driver dashboard state, location monitoring, vehicle selection, and home navigation.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.


// Android imports: platform classes and services; these APIs interact with the Android system.
import android.content.Context
import android.content.Intent
import android.location.LocationManager
// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
// Activity-result bridge: this remembers a launcher across recompositions and routes Android intent results back to Compose.
import androidx.activity.compose.rememberLauncherForActivityResult
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
// Hilt imports: dependency injection annotations and ViewModel integration.
import androidx.hilt.navigation.compose.hiltViewModel
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// AndroidX imports: navigation or lifecycle infrastructure used by this file.
import androidx.navigation.NavController
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.location.LocationService
import com.example.rynzodriver.data.location.LocationTrackingState
import com.example.rynzodriver.data.location.requestLocationSettingsResolution
import com.example.rynzodriver.ui.companyTrips.CompanyTripsSection
import com.example.rynzodriver.ui.navigation.Screen
import com.example.rynzodriver.ui.permissions.PermissionHandler
import com.example.rynzodriver.ui.trips.RequestedTripsSection
import com.example.rynzodriver.util.hasLocationPermission

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Opt-in boundary: this declaration intentionally uses an API marked experimental by the library.
@OptIn(ExperimentalMaterial3Api::class)
// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `HomeScreen`: normal synchronous execution is used unless the body starts a coroutine itself.
fun HomeScreen(
    navController: NavController,
// Default dependency: if the caller omits this parameter, Hilt creates or retrieves the ViewModel for this Compose destination.
    viewModel: HomeViewModel = hiltViewModel()
) {
    PermissionHandler()

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val context = LocalContext.current
// Delegated property: `by` delegates reading/writing to the expression to the right. The delegate owns storage and observation semantics.
// State bridge: `collectAsState()` observes the Flow and exposes its latest value as Compose state, so the UI recomposes on each emission.
    val uiState by viewModel.uiState.collectAsState()
// Delegated property: `by` delegates reading/writing to the expression to the right. The delegate owns storage and observation semantics.
// State bridge: `collectAsState()` observes the Flow and exposes its latest value as Compose state, so the UI recomposes on each emission.
    val locationState by LocationTrackingState.state.collectAsState()
// Recomposition memory: `remember` retains this value through recompositions and recreates it only when the composition key changes.
// Sheet state: `remember...` stores bottom-sheet state in Compose memory and `skipPartiallyExpanded` requests direct expanded/hidden behavior.
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
// Recomposition memory: `remember` retains this value through recompositions and recreates it only when the composition key changes.
// Activity-result bridge: this remembers a launcher across recompositions and routes Android intent results back to Compose.
    val locationSettingsLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { }

// Side-effect lifecycle: this coroutine runs when the Composable enters or when any key in parentheses changes.
    LaunchedEffect(uiState.navigationEvent) {
// Safe-call chain: `?.let` runs the block only when the receiver is non-null; inside the block `it` is the non-null value.
        uiState.navigationEvent?.let { event ->
// `when` branch selection: Kotlin evaluates conditions in source order and executes only the first matching branch.
// Branch expression: `when` is an expression and selects the first branch whose condition matches the current state.
            when (event) {
                is HomeNavigationEvent.NavigateToCompanyTrip -> {
                    navController.navigate(Screen.CompanyTripDetail.createRoute(event.trip))
                    viewModel.consumeNavigationEvent()
                }
                is HomeNavigationEvent.NavigateToGenericTrip -> {
                    navController.navigate(Screen.TripDetail.createRoute(event.tripId))
                    viewModel.consumeNavigationEvent()
                }
            }
        }
    }

// Side-effect lifecycle: this coroutine runs when the Composable enters or when any key in parentheses changes.
    LaunchedEffect(uiState.isOnDuty, uiState.selectedVehicleId) {
// `if` guard: the following block executes only while `uiState.isOnDuty` evaluates to `true`; a false result skips it.
        if (uiState.isOnDuty) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val selectedVehicleId = uiState.selectedVehicleId
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
// `if` guard: the following block executes only while `selectedVehicleId.isNullOrBlank()` evaluates to `true`; a false result skips it.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
            if (selectedVehicleId.isNullOrBlank()) {
                viewModel.setOnDuty(false)
                stopLocationTracking(context)
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                return@LaunchedEffect
            }

// `if` guard: the following block executes only while `!context.hasLocationPermission()` evaluates to `true`; a false result skips it.
            if (!context.hasLocationPermission()) {
                viewModel.setOnDuty(false)
                stopLocationTracking(context)
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                return@LaunchedEffect
            }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val locationManager = context.getSystemService(LocationManager::class.java)
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Safe Android call: `?.` avoids dereferencing a null `LocationManager`; the provider result is compared with `true`.
            val gpsEnabled = locationManager?.isProviderEnabled(LocationManager.GPS_PROVIDER) == true
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Safe Android call: `?.` avoids dereferencing a null `LocationManager`; the provider result is compared with `true`.
            val networkEnabled = locationManager?.isProviderEnabled(LocationManager.NETWORK_PROVIDER) == true
// `if` guard: the following block executes only while `!gpsEnabled && !networkEnabled` evaluates to `true`; a false result skips it.
            if (!gpsEnabled && !networkEnabled) {
                viewModel.setOnDuty(false)
                stopLocationTracking(context)
                context.requestLocationSettingsResolution(locationSettingsLauncher)
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                return@LaunchedEffect
            }
            startLocationTracking(context, selectedVehicleId)
        } else {
            stopLocationTracking(context)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .verticalScroll(rememberScrollState())
    ) {
        TopSection(
            viewModel = viewModel,
            currentLocation = locationState.currentLocation,
            lastLocation = locationState.lastLocation,
            userName = "John Doe",
            onSelectVehicleClick = {
// `if` guard: the following block executes only while `uiState.isTripActive` evaluates to `true`; a false result skips it.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                if (uiState.isTripActive) return@TopSection
                viewModel.fetchDriverVehicles()
                viewModel.openVehicleSheet()
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        RequestedTripsSection(
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
            modifier = Modifier.fillMaxWidth(),
            onViewClick = { trip ->
                navController.navigate(Screen.TripDetail.createRoute(trip.id))
            }
        )

        Spacer(modifier = Modifier.height(12.dp))

        CompanyTripsSection(
            trips = uiState.companyTrips,
            isLoading = uiState.isCompanyTripsLoading,
            error = uiState.companyTripsError,
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
            modifier = Modifier.fillMaxWidth(),
            onViewClick = { trip ->
                navController.navigate(Screen.CompanyTripDetail.createRoute(trip))
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
    }

// `if` guard: the following block executes only while `uiState.isVehicleSheetOpen` evaluates to `true`; a false result skips it.
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

// Collection/optional fallback: `ifEmpty` supplies the alternate value only when the collection or string is empty.
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

// Collection iteration: `forEach` executes the lambda once for every item; the item is bound as `it` unless named explicitly.
                uiState.vehicles.forEach { vehicle ->
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                    val vehicleLabel = listOfNotNull(
                        vehicle.vehicleModel,
                        vehicle.vehicleNumber
// Safe search: `firstOrNull` returns the first matching item or null when no item satisfies the condition.
                    ).firstOrNull { it.isNotBlank() }
                        ?: "Vehicle ${vehicle.id.take(4)}"

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable(enabled = !uiState.isTripActive) { viewModel.selectVehicle(vehicle) },
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
// Safe-call chain: `?.let` runs the block only when the receiver is non-null; inside the block `it` is the non-null value.
                                vehicle.vehicleNumber?.let {
                                    Text(
                                        text = it,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

// `if` guard: the following block executes only while `uiState.selectedVehicleId == vehicle.id` evaluates to `true`; a false result skips it.
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

// Function `startLocationTracking`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
private fun startLocationTracking(context: Context, vehicleId: String) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val intent = Intent(context, LocationService::class.java)
    intent.action = LocationService.ACTION_START
    intent.putExtra(LocationService.EXTRA_VEHICLE_ID, vehicleId)
    context.startService(intent)
}

// Function `stopLocationTracking`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
private fun stopLocationTracking(context: Context) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val intent = Intent(context, LocationService::class.java)
    intent.action = LocationService.ACTION_STOP
    context.startService(intent)
}
