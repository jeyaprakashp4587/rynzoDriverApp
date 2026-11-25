package com.example.rynzodriver.util

// Senior explanation:
// Reusable Android or map utility behavior.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file provides reusable Android or route helpers.

// Google Play Services imports: fused location providers, callbacks, and map coordinates.
import com.google.android.gms.maps.model.LatLng

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
val DEFAULT_MAP_CENTER = LatLng(13.0827, 80.2707)

// Data model: `MapRoute` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class MapRoute(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val points: List<LatLng>,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val fallbackStops: List<String> = emptyList()
) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isValid: Boolean
        get() = points.isNotEmpty()
}

// Function `buildRoutePoints`: normal synchronous execution is used unless the body starts a coroutine itself.
fun buildRoutePoints(
    stops: List<String>,
    center: LatLng = DEFAULT_MAP_CENTER
): List<LatLng> {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val safeStops = if (stops.isEmpty()) {
        listOf("Pickup stop", "Drop-off stop")
    } else {
        stops
    }

// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Collection transformation: `map` returns a new list with one transformed result per item; `mapIndexed` also supplies each index.
    return safeStops.mapIndexed { index, _ ->
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val offset = index * 0.0035
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val lat = center.latitude + offset
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
        val lon = center.longitude + (if (index % 2 == 0) 0.003 else -0.003) * (index + 1)
        LatLng(lat, lon)
    }
}

// Function `buildMapRoute`: normal synchronous execution is used unless the body starts a coroutine itself.
fun buildMapRoute(
    stops: List<String>,
    center: LatLng = DEFAULT_MAP_CENTER
): MapRoute {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val safeStops = if (stops.isEmpty()) {
        listOf("Pickup stop", "Drop-off stop")
    } else {
        stops
    }

// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
    return MapRoute(
        points = buildRoutePoints(safeStops, center),
        fallbackStops = safeStops
    )
}
