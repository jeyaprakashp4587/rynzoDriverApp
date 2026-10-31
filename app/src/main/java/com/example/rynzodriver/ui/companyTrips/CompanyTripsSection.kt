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

private val CompanyCardBackground = Color(0xFFFFFFFF)
private val CompanyCardBlack = Color(0xFF000000)
private val CompanyCardLightGray = Color(0xFFF3F3F3)
private val CompanyCardDivider = Color(0xFFEAEAEA)
private val CompanyCardGray = Color(0xFF6B6B6B)

data class CompanyTripUiModel(
    val id: String,
    val title: String,
    val status: String,
    val routeName: String,
    val vehicleLabel: String,
    val pickupLabel: String,
    val dropoffLabel: String,
    val customerLabel: String
)

fun CompanyTripDto.toUiModel(): CompanyTripUiModel {
    val primaryName = listOfNotNull(
        routeName,
        title,
        name,
        tripType,
        type
    ).firstOrNull { !it.isNullOrBlank() } ?: "Company route"

    val statusLabel = status?.replace("_", " ")
        ?.lowercase()
        ?.replaceFirstChar { it.titlecase() } ?: "Active"

    val pickupLabel = stops.firstOrNull { it.type.equals("pickup", ignoreCase = true) || it.stopType.equals("pickup", ignoreCase = true) }
        ?.locationName
        ?: pickupLocation?.locationName
        ?: pickupLocation?.name
        ?: "Pickup"

    val dropoffLabel = stops.firstOrNull { it.type.equals("drop", ignoreCase = true) || it.type.equals("dropoff", ignoreCase = true) || it.stopType.equals("drop", ignoreCase = true) || it.stopType.equals("dropoff", ignoreCase = true) }
        ?.locationName
        ?: dropoffLocation?.locationName
        ?: dropoffLocation?.name
        ?: "Drop-off"

    val customerLabel = listOfNotNull(
        customerName,
        recipients.firstOrNull()?.customerName,
        recipients.firstOrNull()?.recipientName,
        recipients.firstOrNull()?.name
    ).firstOrNull { !it.isNullOrBlank() } ?: "Route customer"

    return CompanyTripUiModel(
        id = id,
        title = primaryName,
        status = statusLabel,
        routeName = primaryName,
        vehicleLabel = "Vehicle assigned",
        pickupLabel = pickupLabel,
        dropoffLabel = dropoffLabel,
        customerLabel = customerLabel
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
            color = CompanyCardBlack,
            fontSize = 17.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 8.dp, bottom = 8.dp)
        )

        when {
            isLoading -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = CompanyCardBlack,
                        trackColor = CompanyCardLightGray,
                        strokeWidth = 3.dp
                    )
                }
            }

            !error.isNullOrBlank() -> {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Text(
                        text = error,
                        color = CompanyCardBlack,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            trips.isEmpty() -> {
                Text(
                    text = "No company trips available",
                    color = CompanyCardGray,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
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
                    items(trips) { trip ->
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
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        shape = RoundedCornerShape(12.dp),
        color = CompanyCardBackground,
        border = BorderStroke(1.dp, CompanyCardDivider),
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
                        text = "Company route",
                        color = CompanyCardGray,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(Modifier.height(2.dp))
                    Text(
                        text = trip.title,
                        color = CompanyCardBlack,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Spacer(Modifier.width(12.dp))
                Surface(color = CompanyCardLightGray, shape = RoundedCornerShape(50)) {
                    Text(
                        text = trip.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = CompanyCardBlack,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = CompanyCardGray,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(Modifier.width(6.dp))
                Text(
                    text = trip.vehicleLabel,
                    color = CompanyCardGray,
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Spacer(Modifier.height(10.dp))

            CompanyRouteSpine(
                pickupLabel = trip.pickupLabel,
                dropoffLabel = trip.dropoffLabel
            )

            Spacer(Modifier.height(14.dp))
            HorizontalDivider(color = CompanyCardDivider, thickness = 1.dp)
            Spacer(Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = CompanyCardLightGray,
                    modifier = Modifier.size(40.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = CompanyCardBlack,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = trip.customerLabel,
                        color = CompanyCardBlack,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = trip.routeName,
                        color = CompanyCardGray,
                        fontSize = 11.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
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
                    containerColor = CompanyCardBlack,
                    contentColor = CompanyCardBackground
                )
            ) {
                Text("View", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
            }
        }
    }
}

@Composable
private fun CompanyRouteSpine(
    pickupLabel: String,
    dropoffLabel: String
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        CompanyRouteStopRow(label = "Pickup", value = pickupLabel, showConnector = true)
        CompanyRouteStopRow(label = "Drop-off", value = dropoffLabel, showConnector = false)
    }
}

@Composable
private fun CompanyRouteStopRow(
    label: String,
    value: String,
    showConnector: Boolean
) {
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
                    .background(CompanyCardBlack, CircleShape)
            )
            if (showConnector) {
                Box(
                    modifier = Modifier
                        .width(1.5.dp)
                        .weight(1f)
                        .background(CompanyCardBlack.copy(alpha = 0.25f))
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
                text = label,
                color = CompanyCardGray,
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = value,
                color = CompanyCardBlack,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun CompanyTripsPlaceholder() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "Company Trips", color = CompanyCardBlack)
    }
}
