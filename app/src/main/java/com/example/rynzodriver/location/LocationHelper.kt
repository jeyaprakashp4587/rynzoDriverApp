package com.example.rynzodriver.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

@SuppressLint("MissingPermission")
suspend fun getCurrentOrLastLocation(context: Context): Location? {
    val client: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
    return suspendCancellableCoroutine { cont ->
        client.lastLocation
            .addOnSuccessListener { loc ->
                if (loc != null) {
                    cont.resume(loc)
                } else {
                    val cts = CancellationTokenSource()
                    client.getCurrentLocation(com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY, cts.token)
                        .addOnSuccessListener { current -> cont.resume(current) }
                        .addOnFailureListener { cont.resume(null) }
                    cont.invokeOnCancellation { cts.cancel() }
                }
            }
            .addOnFailureListener { cont.resume(null) }
    }
}
