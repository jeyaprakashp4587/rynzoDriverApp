package com.example.rynzodriver.ui.trips

// Senior explanation:
// Requested-trip presentation, list/detail state, and trip acceptance flow.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements trip requests, trip details, and their Compose UI.

// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.compose.foundation.background
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Hilt imports: dependency injection annotations and ViewModel integration.
import androidx.hilt.navigation.compose.hiltViewModel
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.domain.model.trips.Stop
import com.example.rynzodriver.domain.model.trips.StopType
import com.example.rynzodriver.domain.model.trips.Trip

// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CardBackground = Color(0xFFFFFFFF)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CardBlack = Color(0xFF000000)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CardLightGray = Color(0xFFF3F3F3)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CardDivider = Color(0xFFEAEAEA)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CardGray = Color(0xFF6B6B6B)

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `OrdersScreen`: normal synchronous execution is used unless the body starts a coroutine itself.
fun OrdersScreen() {
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Orders Screen Placeholder", color = CardBlack)
    }
}

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `RequestedTripsCard`: normal synchronous execution is used unless the body starts a coroutine itself.
fun RequestedTripsCard(
    trip: Trip,
    onViewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .widthIn(max = 300.dp)
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(14.dp),
        color = CardBackground,
        border = BorderStroke(1.dp, CardDivider),
        shadowElevation = 0.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Trip request",
                        color = CardGray,
                        fontSize = 10.sp,
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
                        color = CardBlack,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.width(12.dp))
                Surface(color = CardLightGray, shape = RoundedCornerShape(50)) {
                    Text(
                        text = trip.status
                            .replace("_", " ")
                            .lowercase()
                            .replaceFirstChar { it.titlecase() },
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = CardBlack,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.height(16.dp))

            RequestedRouteSpine(stops = trip.stops)

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = CardDivider, thickness = 1.dp)
            Spacer(Modifier.height(14.dp))

            Row(
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = CardLightGray,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = CardBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                        text = trip.customerName ?: "Customer",
                        color = CardBlack,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                    val phone = trip.customerPhone
// `if` guard: the following block executes only while `!phone.isNullOrEmpty()` evaluates to `true`; a false result skips it.
                    if (!phone.isNullOrEmpty()) {
                        Text(
                            text = phone,
                            color = CardGray,
                            fontSize = 11.sp,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            Button(
                onClick = onViewClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
                elevation = null,
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardBlack,
                    contentColor = CardBackground
                )
            ) {
                Text("View", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `RequestedRouteSpine`: normal synchronous execution is used unless the body starts a coroutine itself.
fun RequestedRouteSpine(stops: List<Stop>) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val visibleStops = stops.take(2)
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val hiddenCount = stops.size - visibleStops.size

// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
    Column(modifier = Modifier.fillMaxWidth()) {
        visibleStops.forEachIndexed { index, stop ->
            RouteStopRow(
                stop = stop,
                showConnector = index < visibleStops.lastIndex || hiddenCount > 0
            )
        }
// `if` guard: the following block executes only while `hiddenCount > 0` evaluates to `true`; a false result skips it.
        if (hiddenCount > 0) {
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                            .background(CardBlack.copy(alpha = 0.35f), CircleShape)
                    )
                }
                Spacer(Modifier.width(12.dp))
                Text(
                    text = if (hiddenCount == 1) "1 more stop" else "$hiddenCount more stops",
                    color = CardGray,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `RouteStopRow`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
private fun RouteStopRow(stop: Stop, showConnector: Boolean) {
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
                    .background(CardBlack, if (isPickup) CircleShape else RoundedCornerShape(2.dp))
            )
// `if` guard: the following block executes only while `showConnector` evaluates to `true`; a false result skips it.
            if (showConnector) {
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .weight(1f)
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                        .background(CardBlack.copy(alpha = 0.25f))
                )
            }
        }
        Spacer(Modifier.width(12.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (showConnector) 14.dp else 0.dp)
        ) {
            Text(
                text = if (isPickup) "Pickup" else "Drop-off",
                color = CardGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = stop.locationName,
                color = CardBlack,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `RequestedTripsSection`: normal synchronous execution is used unless the body starts a coroutine itself.
fun RequestedTripsSection(
// Default dependency: if the caller omits this parameter, Hilt creates or retrieves the ViewModel for this Compose destination.
    viewModel: TripRequestsViewModel = hiltViewModel(),
    onViewClick: (Trip) -> Unit = {},
    modifier: Modifier = Modifier
) {
// Delegated property: `by` delegates reading/writing to the expression to the right. The delegate owns storage and observation semantics.
// State bridge: `collectAsState()` observes the Flow and exposes its latest value as Compose state, so the UI recomposes on each emission.
    val uiState by viewModel.uiState.collectAsState()

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "Trip Requests",
            color = CardBlack,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )

        Spacer(modifier = Modifier.height(4.dp))

// `when` branch selection: Kotlin evaluates conditions in source order and executes only the first matching branch.
// Branch expression: `when` is an expression and selects the first branch whose condition matches the current state.
        when {
            uiState.isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = CardBlack,
                        trackColor = CardLightGray,
                        strokeWidth = 3.dp
                    )
                }
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
// Explicit non-null assertion: `!!` tells Kotlin the value cannot be null; null at runtime throws an exception instead.
                        text = uiState.error!!,
                        color = CardBlack,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.fetchTripRequests() },
                        shape = RoundedCornerShape(8.dp),
                        elevation = null,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = CardBlack,
                            contentColor = CardBackground
                        )
                    ) {
                        Text("Retry", fontWeight = FontWeight.SemiBold)
                    }
                }
            }

            uiState.trips.isEmpty() -> {
                Text(
                    text = "No trip requests available",
                    color = CardGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                )
            }

// `else` fallback: this block runs only when the preceding `if` condition did not match.
            else -> {
                LazyRow(
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    items(uiState.trips) { trip ->
                        RequestedTripsCard(
                            trip = trip,
                            onViewClick = { onViewClick(trip) }
                        )
                    }
                }
            }
        }
    }
}
