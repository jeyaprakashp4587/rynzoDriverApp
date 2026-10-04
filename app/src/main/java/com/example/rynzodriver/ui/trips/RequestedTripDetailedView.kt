package com.example.rynzodriver.ui.trips

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.rynzodriver.domain.model.trips.Stop
import com.example.rynzodriver.domain.model.trips.StopType
import com.example.rynzodriver.domain.model.trips.TripDetail
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

private val AppBackground = Color(0xFFFFFFFF)
private val AppBlack = Color(0xFF000000)
private val AppLightGray = Color(0xFFF3F3F3)
private val AppDivider = Color(0xFFEAEAEA)
private val AppGray = Color(0xFF6B6B6B)
private val DefaultCenter = LatLng(13.0827, 80.2707)

private const val LIGHT_MAP_STYLE = """
[
  {"elementType":"geometry","stylers":[{"color":"#f5f5f5"}]},
  {"elementType":"labels.icon","stylers":[{"visibility":"off"}]},
  {"elementType":"labels.text.fill","stylers":[{"color":"#616161"}]},
  {"elementType":"labels.text.stroke","stylers":[{"color":"#f5f5f5"}]},
  {"featureType":"poi","stylers":[{"visibility":"off"}]},
  {"featureType":"transit","stylers":[{"visibility":"off"}]},
  {"featureType":"road","elementType":"geometry","stylers":[{"color":"#ffffff"}]},
  {"featureType":"road.arterial","elementType":"labels.text.fill","stylers":[{"color":"#757575"}]},
  {"featureType":"road.highway","elementType":"geometry","stylers":[{"color":"#fff3c4"}]},
  {"featureType":"road.highway","elementType":"geometry.stroke","stylers":[{"color":"#f2e2a0"}]},
  {"featureType":"water","elementType":"geometry","stylers":[{"color":"#c9e6f5"}]},
  {"featureType":"landscape.natural","elementType":"geometry","stylers":[{"color":"#eef2ee"}]}
]
"""

@Composable
fun RequestedTripDetailScreen(
    tripId: String,
    viewModel: TripRequestsViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(tripId) {
        viewModel.fetchTripDetails(tripId)
    }

    val tripDetail = uiState.selectedTripDetail
    val isLoading = uiState.isDetailLoading
    val error = uiState.detailError
    val isApproving = uiState.isAccepting

    if (tripDetail == null && !isLoading && error == null) return

    val handleApprove = {
        viewModel.acceptTripRequest(tripId, onSuccess = onApprove)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
        when {
            isLoading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = AppBlack,
                trackColor = AppLightGray,
                strokeWidth = 3.dp
            )

            error != null -> ErrorState(
                message = error,
                onDismiss = onDismiss,
                modifier = Modifier.align(Alignment.Center)
            )

            tripDetail != null -> TripContent(
                trip = tripDetail,
                onDismiss = onDismiss,
                onApprove = handleApprove,
                onReject = onReject,
                isApproving = isApproving
            )
        }
    }
}

@Composable
fun RequestedTripDetailBottomSheet(
    tripId: String,
    viewModel: TripRequestsViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false,
            dismissOnClickOutside = false
        )
    ) {
        RequestedTripDetailScreen(
            tripId = tripId,
            viewModel = viewModel,
            onDismiss = onDismiss,
            onApprove = onApprove,
            onReject = onReject
        )
    }
}

@Composable
private fun ErrorState(message: String, onDismiss: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            color = AppBlack,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AppBlack, contentColor = AppBackground)
        ) {
            Text("Close", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
    }
}

@Composable
private fun TripContent(
    trip: TripDetail,
    onDismiss: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    isApproving: Boolean = false
) {
    if (trip.stops.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "No route data available",
                color = AppGray,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
        return
    }

    val points = remember(trip) { trip.stops.map { LatLng(it.latitude, it.longitude) } }
    val camera = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(points.firstOrNull() ?: DefaultCenter, 13f)
    }
    var mapLoaded by remember { mutableStateOf(false) }

    LaunchedEffect(mapLoaded, points) {
        if (!mapLoaded || points.isEmpty()) return@LaunchedEffect
        if (points.size == 1) {
            camera.animate(CameraUpdateFactory.newLatLngZoom(points.first(), 15f))
        } else {
            val bounds = LatLngBounds.builder().apply { points.forEach(::include) }.build()
            camera.animate(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val sheetMaxHeight = maxHeight * 0.55f

        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = camera,
            contentPadding = PaddingValues(top = 96.dp, bottom = sheetMaxHeight),
            properties = MapProperties(
                isMyLocationEnabled = false,
                mapStyleOptions = MapStyleOptions(LIGHT_MAP_STYLE)
            ),
            uiSettings = MapUiSettings(
                zoomControlsEnabled = false,
                compassEnabled = false,
                mapToolbarEnabled = false,
                myLocationButtonEnabled = false
            ),
            onMapLoaded = { mapLoaded = true }
        ) {
            if (points.size > 1) {
                Polyline(
                    points = points,
                    color = AppBlack,
                    width = 7f
                )
            }
            trip.stops.forEachIndexed { index, stop ->
                val markerColor = if (stop.type == StopType.PICKUP) BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_RED
                Marker(
                    state = MarkerState(position = points[index]),
                    title = stop.locationName,
                    snippet = if (stop.type == StopType.PICKUP) "Pickup" else "Drop-off",
                    icon = BitmapDescriptorFactory.defaultMarker(markerColor)
                )
            }
        }

        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(16.dp)
                .shadow(4.dp, CircleShape)
                .size(40.dp),
            colors = IconButtonDefaults.iconButtonColors(containerColor = AppBackground, contentColor = AppBlack)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(20.dp))
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .heightIn(max = sheetMaxHeight),
            shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            color = AppBackground,
            shadowElevation = 12.dp
        ) {
            Column {
                Column(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .padding(top = 8.dp, bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                            .width(36.dp)
                            .height(4.dp)
                            .background(AppDivider, RoundedCornerShape(50))
                    )

                    TripHeader(trip = trip)

                    Spacer(Modifier.height(20.dp))

                    RouteSection(stops = trip.stops)

                    Spacer(Modifier.height(16.dp))

                    HorizontalDivider(color = AppDivider, thickness = 1.dp)

                    CustomerRow(trip = trip)

                    Spacer(Modifier.height(4.dp))
                }

                HorizontalDivider(color = AppDivider, thickness = 1.dp)
                ActionBar(
                    onApprove = onApprove,
                    onReject = onReject,
                    isApproving = isApproving
                )
            }
        }
    }
}

@Composable
private fun TripHeader(trip: TripDetail) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "New trip request",
                color = AppGray,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = trip.tripType
                    ?.replace("_", " ")
                    ?.lowercase()
                    ?.replaceFirstChar { it.titlecase() }
                    ?: "Trip",
                color = AppBlack,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Spacer(Modifier.width(12.dp))
        Surface(color = AppLightGray, shape = RoundedCornerShape(50)) {
            Text(
                text = trip.status,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                color = AppBlack,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun CustomerRow(trip: TripDetail) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(color = AppLightGray, shape = CircleShape, modifier = Modifier.size(44.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = null,
                    tint = AppBlack,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
        Spacer(Modifier.width(14.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = trip.customerName ?: "Customer",
                color = AppBlack,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = trip.customerPhone ?: "No contact",
                color = AppGray,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun RouteSection(stops: List<Stop>) {
    var expanded by remember { mutableStateOf(false) }
    val collapsible = stops.size > 2
    val visibleStops = if (collapsible && !expanded) stops.take(2) else stops

    Column(modifier = Modifier.fillMaxWidth()) {
        visibleStops.forEachIndexed { index, stop ->
            StopRow(stop = stop, isLast = index == visibleStops.lastIndex)
        }

        if (collapsible) {
            TextButton(
                onClick = { expanded = !expanded },
                contentPadding = PaddingValues(start = 32.dp, top = 4.dp, bottom = 4.dp, end = 8.dp)
            ) {
                Text(
                    text = if (expanded) "Show less" else "${stops.size - 2} more stops",
                    color = AppBlack,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp
                )
                Icon(
                    imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = AppBlack,
                    modifier = Modifier.size(18.dp)
                )
            }
        }
    }
}

@Composable
private fun StopRow(stop: Stop, isLast: Boolean) {
    val isPickup = stop.type == StopType.PICKUP

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .width(20.dp)
                .fillMaxHeight()
        ) {
            Spacer(Modifier.height(5.dp))
            Box(
                modifier = Modifier
                    .size(10.dp)
                    .background(AppBlack, if (isPickup) CircleShape else RoundedCornerShape(2.dp))
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .weight(1f)
                        .background(AppBlack.copy(alpha = 0.25f))
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 18.dp)
        ) {
            Text(
                text = if (isPickup) "Pickup" else "Drop-off",
                color = AppGray,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stop.locationName,
                color = AppBlack,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ActionBar(
    onApprove: () -> Unit,
    onReject: () -> Unit,
    isApproving: Boolean = false
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(AppBackground)
            .navigationBarsPadding()
            .padding(horizontal = 20.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Button(
            onClick = onReject,
            enabled = !isApproving,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = null,
            colors = ButtonDefaults.buttonColors(containerColor = AppLightGray, contentColor = AppBlack)
        ) {
            Text("Reject", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
        }
        Button(
            onClick = onApprove,
            enabled = !isApproving,
            modifier = Modifier
                .weight(1f)
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            elevation = null,
            colors = ButtonDefaults.buttonColors(containerColor = AppBlack, contentColor = AppBackground)
        ) {
            if (isApproving) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = AppBackground,
                        trackColor = AppBackground.copy(alpha = 0.35f)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text("Approving...", fontSize = 15.sp, fontWeight = FontWeight.SemiBold)
                }
            } else {
                Text("Approve", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}