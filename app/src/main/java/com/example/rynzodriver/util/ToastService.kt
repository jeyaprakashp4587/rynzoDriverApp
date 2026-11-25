package com.example.rynzodriver.util

// Senior explanation:
// Reusable Android or map utility behavior.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file provides reusable Android or route helpers.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.content.Context
import android.widget.Toast
// Hilt imports: dependency injection annotations and ViewModel integration.
import dagger.hilt.android.qualifiers.ApplicationContext
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import javax.inject.Inject
import javax.inject.Singleton

// Application lifetime: Hilt/this framework keeps one shared instance in its matching component.
@Singleton
// Runtime type: `ToastService` owns the properties and methods declared in this class body.
class ToastService @Inject constructor(
// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
    @ApplicationContext private val context: Context
) {
// Function `showToast`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
        Toast.makeText(context, message, duration).show()
    }
}
