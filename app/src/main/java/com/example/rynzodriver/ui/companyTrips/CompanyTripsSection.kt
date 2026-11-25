package com.example.rynzodriver.ui.companyTrips

// Senior explanation:
// Company-trip cards, route presentation, and company-run start flow.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements company-trip cards and company-trip detail UI.

// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
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
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.dto.company.CompanyTripDto

// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CardBackground = Color(0xFFFFFFFF)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CardText = Color(0xFF111111)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CardSecondary = Color(0xFF737373)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CardDivider = Color(0xFFEAEAEA)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val CardSurface = Color(0xFFF6F6F6)

// Data model: `CompanyTripUiModel` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class CompanyTripUiModel(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val id: String,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val routeId: String,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val vehicleId: String,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val driverId: String,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val title: String,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val vehicleLabel: String,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val stops: List<String>
)

// Function `CompanyTripDto.toUiModel`: normal synchronous execution is used unless the body starts a coroutine itself.
fun CompanyTripDto.toUiModel(): CompanyTripUiModel {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val vehicleNumber = vehicle?.vehicleNumber?.trim().orEmpty()
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val vehicleModel = vehicle?.vehicleModel?.trim().orEmpty()

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val vehicleDisplay = listOfNotNull(
        vehicleNumber.ifBlank { null },
        vehicleModel.ifBlank { null }
    ).joinToString(" • ")
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
        .ifBlank { "Vehicle ${vehicleId?.take(6) ?: "assigned"}" }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val routeTitle = listOf(
        routeName,
        title,
        name,
        tripType,
        type
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
    ).firstOrNull { !it.isNullOrBlank() }
        ?: if (isDailyTrip == true) "Daily route" else "Company trip"

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
    // Keep every named stop in the order supplied by the API.
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Filtered transformation: `mapNotNull` transforms each item and excludes results that are null.
    val routeStops = stops.mapNotNull { stop ->
        stop.locationName?.trim()?.takeIf { it.isNotBlank() }
// Collection/optional fallback: `ifEmpty` supplies the alternate value only when the collection or string is empty.
    }.ifEmpty {
        listOfNotNull(
            pickupLocation?.locationName?.trim()?.takeIf { it.isNotBlank() }
                ?: pickupLocation?.name?.trim()?.takeIf { it.isNotBlank() },
            dropoffLocation?.locationName?.trim()?.takeIf { it.isNotBlank() }
                ?: dropoffLocation?.name?.trim()?.takeIf { it.isNotBlank() }
        )
// Collection/optional fallback: `ifEmpty` supplies the alternate value only when the collection or string is empty.
    }.ifEmpty {
        listOf("Stops not available")
    }

// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
    return CompanyTripUiModel(
        id = id,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
        routeId = routeId ?: id,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
        vehicleId = vehicleId ?: vehicle?.id ?: "",
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
        driverId = driverId ?: "",
        title = routeTitle,
        vehicleLabel = vehicleDisplay,
        stops = routeStops
    )
}

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `CompanyTripsSection`: normal synchronous execution is used unless the body starts a coroutine itself.
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
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 10.dp)
        )

// `when` branch selection: Kotlin evaluates conditions in source order and executes only the first matching branch.
// Branch expression: `when` is an expression and selects the first branch whose condition matches the current state.
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

// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
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

// `else` fallback: this block runs only when the preceding `if` condition did not match.
            else -> {
                LazyRow(
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
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

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `CompanyTripCard`: normal synchronous execution is used unless the body starts a coroutine itself.
fun CompanyTripCard(
    trip: CompanyTripUiModel,
    onViewClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .width(300.dp)
            .padding(vertical = 4.dp),
        shape = RoundedCornerShape(14.dp),
        color = CardBackground,
        border = BorderStroke(1.dp, CardDivider),
        shadowElevation = 0.dp
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

// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
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
                    .height(46.dp),
                shape = RoundedCornerShape(8.dp),
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

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `CompanyStopRow`: normal synchronous execution is used unless the body starts a coroutine itself; `private` limits access to this file/type scope.
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
// `if` guard: the following block executes only while `stopNumber == 1` evaluates to `true`; a false result skips it.
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

// `if` guard: the following block executes only while `!isLast` evaluates to `true`; a false result skips it.
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
// `else` fallback: this block runs only when the preceding `if` condition did not match.
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

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `CompanyTripsPlaceholder`: normal synchronous execution is used unless the body starts a coroutine itself.
fun CompanyTripsPlaceholder() {
    Box(
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
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
