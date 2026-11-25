package com.example.rynzodriver

// Senior explanation:
// Android Application entry point that initializes Hilt for the app process.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file initializes the application and Hilt dependency injection.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.app.Application
// Hilt imports: dependency injection annotations and ViewModel integration.
import dagger.hilt.android.HiltAndroidApp

// Framework annotation: metadata changes how Android, Compose, Hilt, Gson, or Room processes the declaration below.
@HiltAndroidApp
// Runtime type: `MainApplication` owns the properties and methods declared in this class body.
class MainApplication : Application()
