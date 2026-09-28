package com.example.rynzodriver.ui.orders

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
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
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rynzodriver.domain.model.trips.StopType
import com.example.rynzodriver.domain.model.trips.TripDetail
import com.example.rynzodriver.domain.model.trips.TripStop
import com.google.android.gms.maps.CameraUpdateFactory
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

private val RapidoYellow = Color(0xFFFFD60A)
private val Ink = Color(0xFF14141F)
private val InkSoft = Color(0xFF6B6F7B)
private val Surface1 = Color(0xFFF6F7F9)
private val Stroke = Color(0xFFE9EBEF)
private val PickupGreen = Color(0xFF1FAB57)
private val DropRed = Color(0xFFE5484D)
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
    tripDetail: TripDetail?,
    isLoading: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    if (tripDetail == null && !isLoading && error == null) return

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        when {
            isLoading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = RapidoYellow,
                trackColor = Stroke
            )

            error != null -> ErrorState(
                message = error,
                onDismiss = onDismiss,
                modifier = Modifier.align(Alignment.Center)
            )

            tripDetail != null -> TripContent(
                trip = tripDetail,
                onDismiss = onDismiss,
                onApprove = onApprove,
                onReject = onReject
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RequestedTripDetailBottomSheet(
    tripDetail: TripDetail?,
    isLoading: Boolean,
    error: String?,
    onDismiss: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
    if (tripDetail == null && !isLoading && error == null) return

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        containerColor = Color.White,
        shape = RectangleShape,
        dragHandle = null,
        windowInsets = WindowInsets(0)
    ) {
        RequestedTripDetailScreen(
            tripDetail = tripDetail,
            isLoading = isLoading,
            error = error,
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
            color = DropRed,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(16.dp))
        Button(
            onClick = onDismiss,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(containerColor = RapidoYellow, contentColor = Ink)
        ) {
            Text("Close", fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun TripContent(
    trip: TripDetail,
    onDismiss: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
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
            camera.animate(CameraUpdateFactory.newLatLngBounds(bounds, 120))
        }
    }

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val sheetMaxHeight = maxHeight * 0.58f

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
                Polyline(points = points, color = Ink, width = 8f)
            }
            trip.stops.forEachIndexed { index, stop ->
                val isPickup = stop.type == StopType.PICKUP
                Marker(
                    state = MarkerState(position = points[index]),
                    title = stop.locationName,
                    snippet = if (isPickup) "Pickup" else "Drop-off",
                    icon = BitmapDescriptorFactory.defaultMarker(
                        if (isPickup) BitmapDescriptorFactory.HUE_GREEN else BitmapDescriptorFactory.HUE_RED
                    )
                )
            }
        }

        IconButton(
            onClick = onDismiss,
            modifier = Modifier
                .align(Alignment.TopStart)
                .statusBarsPadding()
                .padding(16.dp)
                .shadow(6.dp, CircleShape)
                .size(42.dp),
            colors = IconButtonDefaults.iconButtonColors(containerColor = Color.White, contentColor = Ink)
        ) {
            Icon(Icons.Default.Close, contentDescription = "Close", modifier = Modifier.size(20.dp))
        }

        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .heightIn(max = sheetMaxHeight),
            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp),
            color = Color.White,
            shadowElevation = 16.dp
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
                            .padding(top = 10.dp, bottom = 16.dp)
                            .align(Alignment.CenterHorizontally)
                            .width(40.dp)
                            .height(4.dp)
                            .background(Stroke, RoundedCornerShape(50))
                    )

                    TripHeader(trip = trip)

                    Spacer(Modifier.height(16.dp))

                    PeopleCard(trip = trip)

                    Spacer(Modifier.height(16.dp))

                    RouteCard(stops = trip.stops)

                    Spacer(Modifier.height(16.dp))
                }

                ActionBar(onApprove = onApprove, onReject = onReject)
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
                color = InkSoft,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = trip.tripType
                    ?.replace("_", " ")
                    ?.lowercase()
                    ?.replaceFirstChar { it.titlecase() }
                    ?: "Trip",
                color = Ink,
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
        Surface(color = RapidoYellow.copy(alpha = 0.22f), shape = RoundedCornerShape(50)) {
            Text(
                text = trip.status,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                color = Ink,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun PeopleCard(trip: TripDetail) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Surface1,
        border = BorderStroke(1.dp, Stroke)
    ) {
        Column(modifier = Modifier.padding(horizontal = 14.dp, vertical = 4.dp)) {
            InfoRow(
                icon = Icons.Default.Person,
                label = "Customer",
                title = trip.customerName ?: "Customer",
                subtitle = trip.customerPhone ?: "No contact"
            )
            HorizontalDivider(color = Stroke)
            InfoRow(
                icon = Icons.Default.DirectionsCar,
                label = "Vehicle",
                title = trip.vehicleNumber ?: "Vehicle",
                subtitle = trip.vehicleModel ?: "Vehicle model"
            )
            HorizontalDivider(color = Stroke)
            InfoRow(
                icon = Icons.Default.Phone,
                label = "Driver",
                title = trip.driverName ?: "Driver",
                subtitle = trip.driverPhone ?: "No driver contact"
            )
        }
    }
}

@Composable
private fun InfoRow(icon: ImageVector, label: String, title: String, subtitle: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(color = RapidoYellow, shape = CircleShape, modifier = Modifier.size(40.dp)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(icon, contentDescription = null, tint = Ink, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, color = InkSoft, fontSize = 11.sp, fontWeight = FontWeight.Medium)
            Text(
                text = title,
                color = Ink,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = subtitle,
                color = InkSoft,
                fontSize = 13.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun RouteCard(stops: List<TripStop>) {
    var expanded by remember { mutableStateOf(false) }
    val collapsible = stops.size > 2
    val visibleStops = if (collapsible && !expanded) stops.take(2) else stops

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Stroke)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Route",
                color = Ink,
                fontSize = 16.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Spacer(Modifier.height(14.dp))

            visibleStops.forEachIndexed { index, stop ->
                StopRow(stop = stop, isLast = index == visibleStops.lastIndex)
            }

            if (collapsible) {
                TextButton(
                    onClick = { expanded = !expanded },
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = if (expanded) "Show less" else "${stops.size - 2} more stops",
                        color = Ink,
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                        contentDescription = null,
                        tint = Ink,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        }
    }
}

@Composable
private fun StopRow(stop: TripStop, isLast: Boolean) {
    val isPickup = stop.type == StopType.PICKUP
    val color = if (isPickup) PickupGreen else DropRed

    Row(modifier = Modifier.fillMaxWidth()) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.width(20.dp)
        ) {
            Spacer(Modifier.height(4.dp))
            Box(
                modifier = Modifier
                    .size(12.dp)
                    .background(color, if (isPickup) CircleShape else RoundedCornerShape(3.dp))
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(36.dp)
                        .background(Stroke)
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 14.dp)
        ) {
            Text(
                text = if (isPickup) "Pickup" else "Drop-off",
                color = color,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = stop.locationName,
                color = Ink,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun ActionBar(onApprove: () -> Unit, onReject: () -> Unit) {
    Surface(color = Color.White, shadowElevation = 8.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedButton(
                onClick = onReject,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                border = BorderStroke(1.5.dp, Ink),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = Ink)
            ) {
                Text("Reject", fontSize = 15.sp, fontWeight = FontWeight.Bold)
            }
            Button(
                onClick = onApprove,
                modifier = Modifier
                    .weight(1f)
                    .height(52.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = RapidoYellow, contentColor = Ink)
            ) {
                Text("Approve", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
            }
        }
    }
}