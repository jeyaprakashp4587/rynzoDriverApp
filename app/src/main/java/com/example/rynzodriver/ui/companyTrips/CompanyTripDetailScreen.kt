package com.example.rynzodriver.ui.companyTrips

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
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rynzodriver.data.api.ApiService
import com.example.rynzodriver.data.dto.company.CompanyRunStartRequest
import com.example.rynzodriver.util.DEFAULT_MAP_CENTER
import com.example.rynzodriver.util.buildMapRoute
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private val CompanyDetailBg = Color(0xFFF7F7F7)
private val CompanyDetailCard = Color(0xFFFFFFFF)
private val CompanyDetailText = Color(0xFF111111)
private val CompanyDetailSubText = Color(0xFF6C6C6C)
private val CompanyDetailDivider = Color(0xFFEAEAEA)
private val CompanyDetailAccent = Color(0xFF0E172A)
private val CompanyDetailSurface = Color(0xFFF4F4F4)

data class CompanyTripDetailUiState(
    val isStarting: Boolean = false,
    val successMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class CompanyTripDetailViewModel @Inject constructor(
    private val apiService: ApiService
) : ViewModel() {

    private val _uiState = MutableStateFlow(CompanyTripDetailUiState())
    val uiState: StateFlow<CompanyTripDetailUiState> = _uiState.asStateFlow()

    fun startCompanyRun(routeId: String, vehicleId: String) {
        if (routeId.isBlank() || vehicleId.isBlank()) {
            _uiState.update {
                it.copy(
                    isStarting = false,
                    errorMessage = "Route or vehicle is missing."
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isStarting = true, errorMessage = null, successMessage = null) }

            try {
                val response = apiService.startCompanyRun(
                    routeId = routeId,
                    request = CompanyRunStartRequest(vehicleId = vehicleId)
                )

                if (response.isSuccessful) {
                    _uiState.update {
                        it.copy(
                            isStarting = false,
                            successMessage = response.body()?.message ?: "Trip started successfully"
                        )
                    }
                } else {
                    val errorMessage = response.errorBody()?.string()
                        ?: response.message()
                        ?: "Unable to start company trip."
                    _uiState.update {
                        it.copy(
                            isStarting = false,
                            errorMessage = errorMessage
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isStarting = false,
                        errorMessage = e.message ?: "Something went wrong while starting the trip."
                    )
                }
            }
        }
    }
}

@Composable
fun CompanyTripDetailScreen(
    routeId: String,
    vehicleId: String,
    driverId: String = "",
    title: String = "Company trip",
    vehicleLabel: String = "Vehicle",
    stops: List<String> = emptyList(),
    onBack: () -> Unit,
    viewModel: CompanyTripDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val routeStops = stops.ifEmpty { listOf("Pickup stop", "Drop-off stop") }
    val mapRoute = remember(routeStops) { buildMapRoute(routeStops, DEFAULT_MAP_CENTER) }
    val points = mapRoute.points
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(points.firstOrNull() ?: DEFAULT_MAP_CENTER, 12f)
    }
    var mapReady by remember { mutableStateOf(false) }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(CompanyDetailBg)
            .statusBarsPadding()
    ) {
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
            if (points.size > 1) {
                Polyline(points = points, color = CompanyDetailAccent, width = 7f)
            }

            points.forEachIndexed { index, latLng ->
                Marker(
                    state = MarkerState(position = latLng),
                    title = if (index == 0) "Pickup" else if (index == points.lastIndex) "Drop-off" else "Stop ${index + 1}",
                    snippet = routeStops.getOrNull(index) ?: "Stop",
                    icon = BitmapDescriptorFactory.defaultMarker(
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

                val errorMessage = uiState.errorMessage
                val successMessage = uiState.successMessage

                if (!errorMessage.isNullOrBlank()) {
                    Text(
                        text = errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.padding(vertical = 12.dp)
                    )
                }

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
                    if (uiState.isStarting) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            strokeWidth = 2.dp,
                            color = Color.White,
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

        LaunchedEffect(points, mapReady) {
            if (!mapReady || points.isEmpty()) return@LaunchedEffect
            if (points.size == 1) {
                cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(points.first(), 15f))
            } else {
                val bounds = LatLngBounds.builder().apply { points.forEach(::include) }.build()
                cameraPositionState.animate(CameraUpdateFactory.newLatLngBounds(bounds, 120))
            }
        }
    }
}

@Composable
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
                if (stopNumber == 1) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(8.dp)
                    )
                }
            }

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

@Composable
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
