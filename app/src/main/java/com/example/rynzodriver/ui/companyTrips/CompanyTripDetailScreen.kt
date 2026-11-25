package com.example.rynzodriver.ui.companyTrips

// Senior explanation:
// Company-trip cards, route presentation, and company-run start flow.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements company-trip cards and company-trip detail UI.

// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Hilt imports: dependency injection annotations and ViewModel integration.
import androidx.hilt.navigation.compose.hiltViewModel
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// AndroidX imports: navigation or lifecycle infrastructure used by this file.
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.api.ApiService
import com.example.rynzodriver.data.dto.company.CompanyRunStartRequest
import com.example.rynzodriver.util.DEFAULT_MAP_CENTER
import com.example.rynzodriver.util.buildMapRoute
// Google Play Services imports: fused location providers, callbacks, and map coordinates.
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
// Maps imports: map state, camera control, coordinates, markers, and route overlays.
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
// Hilt imports: dependency injection annotations and ViewModel integration.
import dagger.hilt.android.lifecycle.HiltViewModel
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CompanyDetailBg = Color(0xFFF7F7F7)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CompanyDetailCard = Color(0xFFFFFFFF)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CompanyDetailText = Color(0xFF111111)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CompanyDetailSubText = Color(0xFF6C6C6C)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CompanyDetailDivider = Color(0xFFEAEAEA)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CompanyDetailAccent = Color(0xFF0E172A)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CompanyDetailSurface = Color(0xFFF4F4F4)

// Data model: `CompanyTripDetailUiState` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CompanyTripDetailUiState(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isStarting: Boolean = false,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val successMessage: String? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val errorMessage: String? = null
)

// Hilt integration: the framework constructs this ViewModel and injects annotated dependencies.
@HiltViewModel
// Runtime type: `CompanyTripDetailViewModel` owns the properties and methods declared in this class body.
class CompanyTripDetailViewModel @Inject constructor(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val apiService: ApiService
) : ViewModel() {

// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
    private val _uiState = MutableStateFlow(CompanyTripDetailUiState())
// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Read-only state exposure: callers can collect but should not mutate this flow; mutation remains inside the owning class.
    val uiState: StateFlow<CompanyTripDetailUiState> = _uiState.asStateFlow()

// Function `startCompanyRun`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun startCompanyRun(routeId: String, vehicleId: String) {
// `if` guard: the following block executes only while `routeId.isBlank() || vehicleId.isBlank()` evaluates to `true`; a false result skips it.
        if (routeId.isBlank() || vehicleId.isBlank()) {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
            _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                it.copy(
                    isStarting = false,
                    errorMessage = "Route or vehicle is missing."
                )
            }
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
            return
        }

// Coroutine lifecycle: `viewModelScope` starts work tied to this ViewModel and cancels it before a ViewModel leak can occur.
        viewModelScope.launch {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
            _uiState.update { it.copy(isStarting = true, errorMessage = null, successMessage = null) }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// `try` boundary: execution continues normally for success; any thrown exception may be captured by a matching `catch`.
// Exception boundary: operations here may throw; success continues normally and matching `catch` blocks handle failures.
            try {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val response = apiService.startCompanyRun(
                    routeId = routeId,
                    request = CompanyRunStartRequest(vehicleId = vehicleId)
                )

// `if` guard: the following block executes only while `response.isSuccessful` evaluates to `true`; a false result skips it.
                if (response.isSuccessful) {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                    _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                        it.copy(
                            isStarting = false,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                            successMessage = response.body()?.message ?: "Trip started successfully"
                        )
                    }
                } else {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                    val errorMessage = response.errorBody()?.string()
                        ?: response.message()
                        ?: "Unable to start company trip."
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                    _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                        it.copy(
                            isStarting = false,
                            errorMessage = errorMessage
                        )
                    }
                }
// Failure recovery: this catch block receives the thrown exception and can update state, notify the UI, or rethrow.
            } catch (e: Exception) {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    it.copy(
                        isStarting = false,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                        errorMessage = e.message ?: "Something went wrong while starting the trip."
                    )
                }
            }
        }
    }
}

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `CompanyTripDetailScreen`: normal synchronous execution is used unless the body starts a coroutine itself.
fun CompanyTripDetailScreen(
    routeId: String,
    vehicleId: String,
    driverId: String = "",
    title: String = "Company trip",
    vehicleLabel: String = "Vehicle",
    stops: List<String> = emptyList(),
    onBack: () -> Unit,
// Default dependency: if the caller omits this parameter, Hilt creates or retrieves the ViewModel for this Compose destination.
    viewModel: CompanyTripDetailViewModel = hiltViewModel()
) {
// Delegated property: `by` delegates reading/writing to the expression to the right. The delegate owns storage and observation semantics.
// State bridge: `collectAsState()` observes the Flow and exposes its latest value as Compose state, so the UI recomposes on each emission.
    val uiState by viewModel.uiState.collectAsState()
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Collection/optional fallback: `ifEmpty` supplies the alternate value only when the collection or string is empty.
    val routeStops = stops.ifEmpty { listOf("Pickup stop", "Drop-off stop") }
// Recomposition memory: `remember` retains this value through recompositions and recreates it only when the composition key changes.
    val mapRoute = remember(routeStops) { buildMapRoute(routeStops, DEFAULT_MAP_CENTER) }
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val points = mapRoute.points
// Recomposition memory: `remember` retains this value through recompositions and recreates it only when the composition key changes.
    val cameraPositionState = rememberCameraPositionState {
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
        position = CameraPosition.fromLatLngZoom(points.firstOrNull() ?: DEFAULT_MAP_CENTER, 12f)
    }
// Mutable property: `var` permits reassignment. Any observer is responsible for reacting if this value changes.
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
    var mapReady by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(CompanyDetailBg)
            .statusBarsPadding()
    ) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val sheetMaxHeight = maxHeight * 0.52f

        GoogleMap(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = sheetMaxHeight),
            cameraPositionState = cameraPositionState,
            properties = MapProperties(isMyLocationEnabled = false),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false
            ),
            onMapLoaded = { mapReady = true }
        ) {
// `if` guard: the following block executes only while `points.size > 1` evaluates to `true`; a false result skips it.
            if (points.size > 1) {
                Polyline(points = points, color = CompanyDetailAccent, width = 7f)
            }

            points.forEachIndexed { index, latLng ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = if (index == 0) "Pickup" else if (index == points.lastIndex) "Drop-off" else "Stop ${index + 1}",
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                    snippet = routeStops.getOrNull(index) ?: "Stop",
                    icon = BitmapDescriptorFactory.defaultMarker(
// `if` guard: the following block executes only while `index == 0) BitmapDescriptorFactory.HUE_GREEN else if (index == points.lastIndex` evaluates to `true`; a false result skips it.
                        if (index == 0) BitmapDescriptorFactory.HUE_GREEN else if (index == points.lastIndex) BitmapDescriptorFactory.HUE_RED else BitmapDescriptorFactory.HUE_AZURE
                    )
                )
            }
        }

        IconButton(
            onClick = onBack,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(start = 16.dp, top = 12.dp)
                .background(CompanyDetailCard, CircleShape)
                .size(42.dp)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = CompanyDetailText
            )
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .heightIn(max = sheetMaxHeight),
            shape = RoundedCornerShape(topStart = 22.dp, topEnd = 22.dp),
            color = CompanyDetailCard,
            shadowElevation = 12.dp
        ) {
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 18.dp, vertical = 14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .width(42.dp)
                        .height(4.dp)
                        .background(CompanyDetailDivider, RoundedCornerShape(50))
                )

                Spacer(Modifier.height(14.dp))

                Text(
                    text = title,
                    color = CompanyDetailText,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(CompanyDetailSurface, RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = CompanyDetailText,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Text(
                        text = vehicleLabel.ifBlank { vehicleId.ifBlank { "Vehicle" } },
                        color = CompanyDetailText,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(16.dp))

                DetailRow(label = "Route ID", value = routeId.ifBlank { "-" })
                DetailRow(label = "Driver ID", value = driverId.ifBlank { "-" })
                DetailRow(label = "Vehicle ID", value = vehicleId.ifBlank { "-" })

                Spacer(Modifier.height(12.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = CompanyDetailSubText,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (driverId.isBlank()) "Driver assigned" else "Driver attached",
                        color = CompanyDetailSubText,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(Modifier.height(16.dp))
                HorizontalDivider(color = CompanyDetailDivider, thickness = 1.dp)
                Spacer(Modifier.height(16.dp))

                Text(
                    text = "Route stops",
                    color = CompanyDetailSubText,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )

                Spacer(Modifier.height(12.dp))

                routeStops.forEachIndexed { index, stopName ->
                    CompanyDetailStopRow(
                        stopName = stopName,
                        stopNumber = index + 1,
                        isLast = index == routeStops.lastIndex
                    )
                }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val errorMessage = uiState.errorMessage
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val successMessage = uiState.successMessage

// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
// `if` guard: the following block executes only while `!errorMessage.isNullOrBlank()` evaluates to `true`; a false result skips it.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
// `if` guard: the following block executes only while `!successMessage.isNullOrBlank()` evaluates to `true`; a false result skips it.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
                if (!successMessage.isNullOrBlank()) {
                    Text(
                        text = successMessage,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = { viewModel.startCompanyRun(routeId, vehicleId) },
                    enabled = !uiState.isStarting,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CompanyDetailAccent,
                        contentColor = Color.White
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
// `if` guard: the following block executes only while `uiState.isStarting` evaluates to `true`; a false result skips it.
                    if (uiState.isStarting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = Color.White,
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                            trackColor = Color.White.copy(alpha = 0.3f)
                        )
                        Spacer(Modifier.size(8.dp))
                    }
                    Text(
                        text = if (uiState.isStarting) "Starting trip..." else "Start company trip",
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 15.sp
                    )
                }

                Spacer(Modifier.height(20.dp))
            }
        }

// Side-effect lifecycle: this coroutine runs when the Composable enters or when any key in parentheses changes.
        LaunchedEffect(points, mapReady) {
// `if` guard: the following block executes only while `!mapReady || points.isEmpty()` evaluates to `true`; a false result skips it.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
            if (!mapReady || points.isEmpty()) return@LaunchedEffect
// `if` guard: the following block executes only while `points.size == 1` evaluates to `true`; a false result skips it.
            if (points.size == 1) {
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(points.first(), 15f))
            } else {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val bounds = LatLngBounds.builder().apply { points.forEach(::include) }.build()
                cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 120))
            }
        }
    }
}

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `CompanyDetailStopRow`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
private fun CompanyDetailStopRow(
    stopName: String,
    stopNumber: Int,
    isLast: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = if (isLast) 0.dp else 12.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(24.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(
                        color = if (stopNumber == 1) CompanyDetailText else CompanyDetailSurface,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
// `if` guard: the following block executes only while `stopNumber == 1` evaluates to `true`; a false result skips it.
                if (stopNumber == 1) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(8.dp)
                    )
                }
            }

// `if` guard: the following block executes only while `!isLast` evaluates to `true`; a false result skips it.
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(44.dp)
                        .background(CompanyDetailDivider)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = if (stopNumber == 1) "Pickup" else if (isLast) "Drop-off" else "Stop $stopNumber",
                color = CompanyDetailSubText,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.8.sp
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = stopName,
                color = CompanyDetailText,
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `DetailRow`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
private fun DetailRow(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            color = CompanyDetailSubText,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.7.sp
        )
        Spacer(Modifier.height(4.dp))
        Text(
            text = value,
            color = CompanyDetailText,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )
    }
}
