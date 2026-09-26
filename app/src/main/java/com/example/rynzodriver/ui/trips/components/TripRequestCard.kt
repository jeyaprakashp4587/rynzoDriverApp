package com.example.rynzodriver.ui.trips.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rynzodriver.domain.model.trips.Stop
import com.example.rynzodriver.domain.model.trips.StopType
import com.example.rynzodriver.domain.model.trips.Trip
import com.example.rynzodriver.ui.theme.UberBlue

@Composable
fun TripRequestCard(
    trip: Trip,
    onAcceptClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            Text(
                text = "New Trip Request",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            RouteSpine(stops = trip.stops)
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = trip.status,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.primary
                )
                
                Button(
                    onClick = onAcceptClick,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
                ) {
                    Text("ACCEPT")
                }
            }
        }
    }
}

@Composable
fun RouteSpine(stops: List<Stop>) {
    Column {
        stops.forEachIndexed { index, stop ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Box(
                    modifier = Modifier
                        .width(24.dp)
                        .height(60.dp),
                    contentAlignment = Alignment.TopCenter
                ) {
                    val color = if (stop.type == StopType.PICKUP) UberBlue else Color.Black
                    
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        // Draw circle for stop
                        drawCircle(
                            color = color,
                            radius = 6.dp.toPx(),
                            center = Offset(size.width / 2, 8.dp.toPx())
                        )
                        
                        // Draw line to next stop
                        if (index < stops.size - 1) {
                            drawLine(
                                color = Color.LightGray,
                                start = Offset(size.width / 2, 14.dp.toPx()),
                                end = Offset(size.width / 2, size.height),
                                strokeWidth = 2.dp.toPx()
                            )
                        }
                    }
                }
                
                Column(
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = if (stop.type == StopType.PICKUP) "Pickup" else "Drop-off",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color.Gray
                    )
                    Text(
                        text = stop.locationName,
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 2,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
