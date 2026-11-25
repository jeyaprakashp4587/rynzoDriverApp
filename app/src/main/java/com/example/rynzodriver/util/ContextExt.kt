package com.example.rynzodriver.util

// Senior explanation:
// Reusable Android or map utility behavior.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file provides reusable Android or route helpers.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import androidx.core.content.ContextCompat

// Function `Context.hasLocationPermission`: normal synchronous execution is used unless the body starts a coroutine itself.
fun Context.hasLocationPermission(): Boolean {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
    return ContextCompat.checkSelfPermission(
        this,
        Manifest.permission.ACCESS_COARSE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
}
