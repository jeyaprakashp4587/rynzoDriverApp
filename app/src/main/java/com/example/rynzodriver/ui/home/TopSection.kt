package com.example.rynzodriver.ui.home

// Senior explanation:
// Driver dashboard state, location monitoring, vehicle selection, and home navigation.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.


// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val HeaderBlue = Color(0xFF276EF1)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val HeaderWhite = Color(0xFFFFFFFF)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val HeaderBlack = Color(0xFF000000)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val HeaderGray = Color(0xFF6B6B6B)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val OnlineGreen = Color(0xFF05A357)
// Immutable configuration: `val` gives this value one read-only binding; the expression is resolved when execution reaches this line.
private val OfflineGray = Color(0xFFBDBDBD)

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `TopSection`: normal synchronous execution is used unless the body starts a coroutine itself.
fun TopSection(
    viewModel: HomeViewModel,
    appName: String = "Rynzo",
    userName: String = "Driver",
    currentLocation: String = "Lat: --, Lon: --",
    lastLocation: String = "Lat: --, Lon: --",
    onNotificationsClick: () -> Unit = {},
    onSelectVehicleClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
// Delegated property: `by` delegates reading/writing to the expression to the right. The delegate owns storage and observation semantics.
// State bridge: `collectAsState()` observes the Flow and exposes its latest value as Compose state, so the UI recomposes on each emission.
    val uiState by viewModel.uiState.collectAsState()
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isOnDuty = uiState.isOnDuty
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isTripActive = uiState.isTripActive
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Safe-call chain: `?.let` runs the block only when the receiver is non-null; inside the block `it` is the non-null value.
    val selectedVehicleName = viewModel.selectedVehicle?.let { vehicle ->
// Safe search: `firstOrNull` returns the first matching item or null when no item satisfies the condition.
        listOfNotNull(vehicle.vehicleModel, vehicle.vehicleNumber).firstOrNull { it.isNotBlank() }
            ?: "Vehicle ${vehicle.id.take(4)}"
    }

    Surface(
        modifier = modifier.fillMaxWidth(),
        color = HeaderBlue,
        shape = RoundedCornerShape(bottomStart = 20.dp, bottomEnd = 20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(start = 20.dp, end = 20.dp, top = 12.dp, bottom = 20.dp)
        ) {
            Row(
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = appName,
                    color = HeaderWhite,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                            .background(HeaderWhite.copy(alpha = 0.16f))
                            .clickable(onClick = onNotificationsClick),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = HeaderWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .background(HeaderWhite, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                            text = userName.trim().firstOrNull()?.uppercase() ?: "D",
                            color = HeaderBlue,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(Modifier.height(18.dp))

            Text(
                text = "Hello,",
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                color = HeaderWhite.copy(alpha = 0.72f),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(Modifier.height(2.dp))
            Text(
                text = userName,
                color = HeaderWhite,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )

            Spacer(Modifier.height(16.dp))

            Surface(
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                color = HeaderWhite
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(10.dp)
                                .background(if (isOnDuty) OnlineGreen else OfflineGray, CircleShape)
                        )
                        Spacer(Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isOnDuty) "You're online" else "You're offline",
                                color = HeaderBlack,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(Modifier.height(2.dp))
                            Text(
                                text = if (isOnDuty) "Receiving trip requests" else "Go online to get trips",
                                color = HeaderGray,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    Spacer(Modifier.width(12.dp))

                    Switch(
                        checked = isOnDuty,
                        enabled = !isTripActive,
                        onCheckedChange = { viewModel.setOnDuty(it) },
                        colors = SwitchDefaults.colors(
                            checkedThumbColor = HeaderWhite,
                            checkedTrackColor = HeaderBlack,
                            checkedBorderColor = HeaderBlack,
                            uncheckedThumbColor = HeaderWhite,
                            uncheckedTrackColor = OfflineGray,
                            uncheckedBorderColor = OfflineGray
                        )
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            OutlinedButton(
                onClick = onSelectVehicleClick,
                enabled = !isTripActive,
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = HeaderWhite,
                    contentColor = HeaderBlue
                ),
                border = androidx.compose.foundation.BorderStroke(1.dp, HeaderWhite)
            ) {
                Row(
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        modifier = Modifier.weight(1f),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                            text = if (isTripActive) "${selectedVehicleName ?: "Vehicle"} · On trip" else selectedVehicleName ?: "Select vehicle",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }

                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = if (isTripActive) "Locked" else "Change",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

// `if` guard: the following block executes only while `isOnDuty` evaluates to `true`; a false result skips it.
            if (isOnDuty) {
                Spacer(Modifier.height(12.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                        .background(HeaderWhite.copy(alpha = 0.14f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 12.dp, vertical = 10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = HeaderWhite,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(Modifier.width(8.dp))
                    Text(
                        text = "$currentLocation",
                        color = HeaderWhite,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}
