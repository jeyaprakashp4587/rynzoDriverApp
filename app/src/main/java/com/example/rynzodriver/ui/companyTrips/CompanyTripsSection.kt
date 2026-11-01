package com.example.rynzodriver.ui.companyTrips

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rynzodriver.data.dto.company.CompanyTripDto

private val CardBackground = Color(0xFFFFFFFF)
private val CardText = Color(0xFF111111)
private val CardSecondary = Color(0xFF737373)
private val CardDivider = Color(0xFFEAEAEA)
private val CardSurface = Color(0xFFF6F6F6)

data class CompanyTripUiModel(
    val id: String,
    val routeId: String,
    val vehicleId: String,
    val driverId: String,
    val title: String,
    val vehicleLabel: String,
    val stops: List<String>
)

fun CompanyTripDto.toUiModel(): CompanyTripUiModel {
    val vehicleNumber = vehicle?.vehicleNumber?.trim().orEmpty()
    val vehicleModel = vehicle?.vehicleModel?.trim().orEmpty()

    val vehicleDisplay = listOfNotNull(
        vehicleNumber.ifBlank { null },
        vehicleModel.ifBlank { null }
    ).joinToString(" • ")
        .ifBlank { "Vehicle ${vehicleId?.take(6) ?: "assigned"}" }

    val routeTitle = listOf(
        routeName,
        title,
        name,
        tripType,
        type
    ).firstOrNull { !it.isNullOrBlank() }
        ?: if (isDailyTrip == true) "Daily route" else "Company trip"

    // Keep every named stop in the order supplied by the API.
    val routeStops = stops.mapNotNull { stop ->
        stop.locationName?.trim()?.takeIf { it.isNotBlank() }
    }.ifEmpty {
        listOfNotNull(
            pickupLocation?.locationName?.trim()?.takeIf { it.isNotBlank() }
                ?: pickupLocation?.name?.trim()?.takeIf { it.isNotBlank() },
            dropoffLocation?.locationName?.trim()?.takeIf { it.isNotBlank() }
                ?: dropoffLocation?.name?.trim()?.takeIf { it.isNotBlank() }
        )
    }.ifEmpty {
        listOf("Stops not available")
    }

    return CompanyTripUiModel(
        id = id,
        routeId = routeId ?: id,
        vehicleId = vehicleId ?: vehicle?.id ?: "",
        driverId = driverId ?: "",
        title = routeTitle,
        vehicleLabel = vehicleDisplay,
        stops = routeStops
    )
}

@Composable
fun CompanyTripsSection(
    trips: List<CompanyTripUiModel>,
    isLoading: Boolean = false,
    error: String? = null,
    modifier: Modifier = Modifier,
    onViewClick: (CompanyTripUiModel) -> Unit = {}
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Company trips",
            color = CardText,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 10.dp)
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 28.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = CardText,
                        trackColor = CardSurface,
                        strokeWidth = 3.dp
                    )
                }
            }

            !error.isNullOrBlank() -> {
                Text(
                    text = error,
                    color = CardSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }

            trips.isEmpty() -> {
                Text(
                    text = "No company trips available",
                    color = CardSecondary,
                    fontSize = 14.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                )
            }

            else -> {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp)
                ) {
                    items(trips, key = { it.id }) { trip ->
                        CompanyTripCard(
                            trip = trip,
                            onViewClick = { onViewClick(trip) }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun CompanyTripCard(
    trip: CompanyTripUiModel,
    onViewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(310.dp)
            .padding(vertical = 5.dp),
        shape = RoundedCornerShape(18.dp),
        color = CardBackground,
        border = BorderStroke(1.dp, CardDivider),
        shadowElevation = 1.dp
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Text(
                text = trip.title,
                color = CardText,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(14.dp))

            // Vehicle information is kept compact and easy to scan.
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(CardSurface, RoundedCornerShape(10.dp))
                    .padding(horizontal = 11.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = "Vehicle",
                    tint = CardText,
                    modifier = Modifier.size(19.dp)
                )
                Spacer(Modifier.width(9.dp))
                Text(
                    text = trip.vehicleLabel,
                    color = CardText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "ROUTE",
                color = CardSecondary,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )

            Spacer(Modifier.height(12.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                trip.stops.forEachIndexed { index, stop ->
                    CompanyStopRow(
                        stopName = stop,
                        stopNumber = index + 1,
                        isLast = index == trip.stops.lastIndex
                    )
                }
            }

            Spacer(Modifier.height(16.dp))
            HorizontalDivider(color = CardDivider, thickness = 1.dp)
            Spacer(Modifier.height(14.dp))

            Button(
                onClick = onViewClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = CardText,
                    contentColor = CardBackground
                ),
                contentPadding = PaddingValues(horizontal = 18.dp)
            ) {
                Text(
                    text = "View trip",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}

@Composable
private fun CompanyStopRow(
    stopName: String,
    stopNumber: Int,
    isLast: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(IntrinsicSize.Min),
        verticalAlignment = Alignment.Top
    ) {
        Column(
            modifier = Modifier
                .width(24.dp)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .padding(top = 2.dp)
                    .size(19.dp)
                    .background(
                        color = if (stopNumber == 1) CardText else CardSurface,
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (stopNumber == 1) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Starting point",
                        tint = CardBackground,
                        modifier = Modifier.size(12.dp)
                    )
                } else {
                    Text(
                        text = stopNumber.toString(),
                        color = CardText,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            if (!isLast) {
                Box(
                    modifier = Modifier
                        .padding(top = 3.dp, bottom = 3.dp)
                        .width(1.5.dp)
                        .weight(1f)
                        .background(CardDivider)
                )
            }
        }

        Spacer(Modifier.width(10.dp))

        Column(
            modifier = Modifier
                .weight(1f)
                .padding(bottom = if (isLast) 0.dp else 18.dp)
        ) {
            Text(
                text = when {
                    stopNumber == 1 -> "PICKUP"
                    isLast -> "FINAL STOP"
                    else -> "STOP $stopNumber"
                },
                color = CardSecondary,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.7.sp
            )
            Spacer(Modifier.height(3.dp))
            Text(
                text = stopName,
                color = CardText,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                lineHeight = 18.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CompanyTripsPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Company Trips",
            color = CardText,
            fontWeight = FontWeight.SemiBold
        )
    }
}
