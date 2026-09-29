package com.example.rynzodriver.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.rynzodriver.ui.permissions.PermissionHandler
import com.example.rynzodriver.ui.trips.RequestedTripsSection

@Composable
fun HomeScreen(
    onViewRequestsClick: () -> Unit = {},
    onViewTripClick: (String) -> Unit = {}
) {
    PermissionHandler()

    val (isOnDuty, setOnDuty) = remember { mutableStateOf(false) }
    // placeholder location; replace with real location lookup later
    val currentLocationName = "Unknown Location"
    Column(
        modifier = Modifier
            .fillMaxSize()
            // .padding(16.dp)
    ) {
        TopSection(
            isOnDuty = isOnDuty,
            onDutyChange = { setOnDuty(it) },
            userName = "John Doe",
            locationName = if (isOnDuty) currentLocationName else null
        )

        Spacer(modifier = Modifier.height(12.dp))

        RequestedTripsSection(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            onViewClick = { trip -> onViewTripClick(trip.id) }
        )
    }
}
