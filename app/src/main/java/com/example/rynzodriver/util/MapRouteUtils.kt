package com.example.rynzodriver.util

import com.google.android.gms.maps.model.LatLng

val DEFAULT_MAP_CENTER = LatLng(13.0827, 80.2707)

data class MapRoute(
    val points: List<LatLng>,
    val fallbackStops: List<String> = emptyList()
) {
    val isValid: Boolean
        get() = points.isNotEmpty()
}

fun buildRoutePoints(
    stops: List<String>,
    center: LatLng = DEFAULT_MAP_CENTER
): List<LatLng> {
    val safeStops = if (stops.isEmpty()) {
        listOf("Pickup stop", "Drop-off stop")
    } else {
        stops
    }

    return safeStops.mapIndexed { index, _ ->
        val offset = index * 0.0035
        val lat = center.latitude + offset
        val lon = center.longitude + (if (index % 2 == 0) 0.003 else -0.003) * (index + 1)
        LatLng(lat, lon)
    }
}

fun buildMapRoute(
    stops: List<String>,
    center: LatLng = DEFAULT_MAP_CENTER
): MapRoute {
    val safeStops = if (stops.isEmpty()) {
        listOf("Pickup stop", "Drop-off stop")
    } else {
        stops
    }

    return MapRoute(
        points = buildRoutePoints(safeStops, center),
        fallbackStops = safeStops
    )
}
