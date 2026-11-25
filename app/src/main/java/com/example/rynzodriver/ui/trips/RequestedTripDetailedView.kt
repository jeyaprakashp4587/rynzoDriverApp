package com.example.rynzodriver.ui.trips

// Senior explanation:
// Requested-trip presentation, list/detail state, and trip acceptance flow.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements trip requests, trip details, and their Compose UI.

// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
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
// Hilt imports: dependency injection annotations and ViewModel integration.
import androidx.hilt.navigation.compose.hiltViewModel
// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
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
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.domain.model.trips.Stop
import com.example.rynzodriver.domain.model.trips.StopType
import com.example.rynzodriver.domain.model.trips.TripDetail
// Google Play Services imports: fused location providers, callbacks, and map coordinates.
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
import com.google.android.gms.maps.model.MapStyleOptions
// Maps imports: map state, camera control, coordinates, markers, and route overlays.
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.Polyline
import com.google.maps.android.compose.rememberCameraPositionState

// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val AppBackground = Color(0xFFFFFFFF)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val AppBlack = Color(0xFF000000)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val AppLightGray = Color(0xFFF3F3F3)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val AppDivider = Color(0xFFEAEAEA)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val AppGray = Color(0xFF6B6B6B)
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
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

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `RequestedTripDetailScreen`: normal synchronous execution is used unless the body starts a coroutine itself.
fun RequestedTripDetailScreen(
    tripId: String,
// Default dependency: if the caller omits this parameter, Hilt creates or retrieves the ViewModel for this Compose destination.
    viewModel: TripRequestsViewModel = hiltViewModel(),
    onDismiss: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit
) {
// Delegated property: `by` delegates reading/writing to the expression to the right. The delegate owns storage and observation semantics.
// State bridge: `collectAsState()` observes the Flow and exposes its latest value as Compose state, so the UI recomposes on each emission.
    val uiState by viewModel.uiState.collectAsState()

// Side-effect lifecycle: this coroutine runs when the Composable enters or when any key in parentheses changes.
    LaunchedEffect(tripId) {
        viewModel.fetchTripDetails(tripId)
    }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val tripDetail = uiState.selectedTripDetail
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isLoading = uiState.isDetailLoading
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val error = uiState.detailError
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isApproving = uiState.isAccepting

// `if` guard: the following block executes only while `tripDetail == null && !isLoading && error == null` evaluates to `true`; a false result skips it.
    if (tripDetail == null && !isLoading && error == null) return

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val handleApprove = {
        viewModel.acceptTripRequest(tripId, onSuccess = onApprove)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground)
    ) {
// `when` branch selection: Kotlin evaluates conditions in source order and executes only the first matching branch.
// Branch expression: `when` is an expression and selects the first branch whose condition matches the current state.
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

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `RequestedTripDetailBottomSheet`: normal synchronous execution is used unless the body starts a coroutine itself.
fun RequestedTripDetailBottomSheet(
    tripId: String,
// Default dependency: if the caller omits this parameter, Hilt creates or retrieves the ViewModel for this Compose destination.
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

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `ErrorState`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
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

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `TripContent`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
private fun TripContent(
    trip: TripDetail,
    onDismiss: () -> Unit,
    onApprove: () -> Unit,
    onReject: () -> Unit,
    isApproving: Boolean = false
) {
// `if` guard: the following block executes only while `trip.stops.isEmpty()` evaluates to `true`; a false result skips it.
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
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
        return
    }

// Recomposition memory: `remember` retains this value through recompositions and recreates it only when the composition key changes.
// Collection transformation: `map` returns a new list with one transformed result per item; `mapIndexed` also supplies each index.
    val points = remember(trip) { trip.stops.map { LatLng(it.latitude, it.longitude) } }
// Recomposition memory: `remember` retains this value through recompositions and recreates it only when the composition key changes.
    val camera = rememberCameraPositionState {
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
        position = CameraPosition.fromLatLngZoom(points.firstOrNull() ?: DefaultCenter, 13f)
    }
// Mutable property: `var` permits reassignment. Any observer is responsible for reacting if this value changes.
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
    var mapLoaded by remember { mutableStateOf(false) }

// Side-effect lifecycle: this coroutine runs when the Composable enters or when any key in parentheses changes.
    LaunchedEffect(mapLoaded, points) {
// `if` guard: the following block executes only while `!mapLoaded || points.isEmpty()` evaluates to `true`; a false result skips it.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
        if (!mapLoaded || points.isEmpty()) return@LaunchedEffect
// `if` guard: the following block executes only while `points.size == 1` evaluates to `true`; a false result skips it.
        if (points.size == 1) {
            camera.animate(CameraUpdateFactory.newLatLngZoom(points.first(), 15f))
        } else {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val bounds = LatLngBounds.builder().apply { points.forEach(::include) }.build()
            camera.animate(CameraUpdateFactory.newLatLngBounds(bounds, 100))
        }
    }

// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val sheetMaxHeight = maxHeight * 0.55f

        GoogleMap(
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
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
// `if` guard: the following block executes only while `points.size > 1` evaluates to `true`; a false result skips it.
            if (points.size > 1) {
                Polyline(
                    points = points,
                    color = AppBlack,
                    width = 7f
                )
            }
            trip.stops.forEachIndexed { index, stop ->
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
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

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `TripHeader`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
private fun TripHeader(trip: TripDetail) {
    Row(
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
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
// Safe chain: each `?.` step runs only if the receiver is non-null; a null earlier in the chain skips the remaining chain safely.
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

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `CustomerRow`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
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
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                text = trip.customerName ?: "Customer",
                color = AppBlack,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                text = trip.customerPhone ?: "No contact",
                color = AppGray,
                fontSize = 14.sp,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `RouteSection`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
private fun RouteSection(stops: List<Stop>) {
// Mutable property: `var` permits reassignment. Any observer is responsible for reacting if this value changes.
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
    var expanded by remember { mutableStateOf(false) }
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val collapsible = stops.size > 2
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val visibleStops = if (collapsible && !expanded) stops.take(2) else stops

// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
    Column(modifier = Modifier.fillMaxWidth()) {
        visibleStops.forEachIndexed { index, stop ->
            StopRow(stop = stop, isLast = index == visibleStops.lastIndex)
        }

// `if` guard: the following block executes only while `collapsible` evaluates to `true`; a false result skips it.
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

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `StopRow`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
private fun StopRow(stop: Stop, isLast: Boolean) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
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
// `if` guard: the following block executes only while `!isLast` evaluates to `true`; a false result skips it.
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .weight(1f)
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
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

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `ActionBar`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
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
// `if` guard: the following block executes only while `isApproving` evaluates to `true`; a false result skips it.
            if (isApproving) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = AppBackground,
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
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
