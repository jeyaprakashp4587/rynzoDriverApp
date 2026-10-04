package com.example.rynzodriver.ui.trips

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
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.rynzodriver.domain.model.trips.Stop
import com.example.rynzodriver.domain.model.trips.StopType
import com.example.rynzodriver.domain.model.trips.Trip

private val CardBackground = Color(0xFFFFFFFF)
private val CardBlack = Color(0xFF000000)
private val CardLightGray = Color(0xFFF3F3F3)
private val CardDivider = Color(0xFFEAEAEA)
private val CardGray = Color(0xFF6B6B6B)

@Composable
fun OrdersScreen() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Orders Screen Placeholder", color = CardBlack)
    }
}

@Composable
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
        shape = RoundedCornerShape(12.dp),
        color = CardBackground,
        border = BorderStroke(1.dp, CardDivider),
        shadowElevation = 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
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
                        text = trip.customerName ?: "Customer",
                        color = CardBlack,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    val phone = trip.customerPhone
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
                    .height(48.dp),
                shape = RoundedCornerShape(10.dp),
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

@Composable
fun RequestedRouteSpine(stops: List<Stop>) {
    val visibleStops = stops.take(2)
    val hiddenCount = stops.size - visibleStops.size

    Column(modifier = Modifier.fillMaxWidth()) {
        visibleStops.forEachIndexed { index, stop ->
            RouteStopRow(
                stop = stop,
                showConnector = index < visibleStops.lastIndex || hiddenCount > 0
            )
        }
        if (hiddenCount > 0) {
            Row(modifier = Modifier.fillMaxWidth()) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(20.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
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

@Composable
private fun RouteStopRow(stop: Stop, showConnector: Boolean) {
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
            if (showConnector) {
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .weight(1f)
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

@Composable
fun RequestedTripsSection(
    viewModel: TripRequestsViewModel = hiltViewModel(),
    onViewClick: (Trip) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Text(
                text = "Trip Requests",
                color = CardBlack,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
            )

            Spacer(modifier = Modifier.height(4.dp))
        }

        when {
            uiState.isLoading -> {
                CircularProgressIndicator(
                    color = CardBlack,
                    trackColor = CardLightGray,
                    strokeWidth = 3.dp
                )
            }

            uiState.error != null -> {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = uiState.error!!,
                        color = CardBlack,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(16.dp))
                    Button(
                        onClick = { viewModel.fetchTripRequests() },
                        shape = RoundedCornerShape(10.dp),
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
                    modifier = Modifier.padding(24.dp)
                )
            }

            else -> {
                LazyRow(
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