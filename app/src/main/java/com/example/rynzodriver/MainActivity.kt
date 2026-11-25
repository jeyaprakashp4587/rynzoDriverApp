package com.example.rynzodriver

// Senior explanation:
// Android activity entry point that attaches the Compose UI and root navigation graph.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file is the Android activity entry point for the Compose application.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.os.Bundle
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import androidx.activity.ComponentActivity
// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.activity.compose.setContent
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
import androidx.activity.enableEdgeToEdge
// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// AndroidX imports: navigation or lifecycle infrastructure used by this file.
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.ui.navigation.RootNavGraph
import com.example.rynzodriver.ui.theme.RynzoDriverTheme
// Hilt imports: dependency injection annotations and ViewModel integration.
import dagger.hilt.android.AndroidEntryPoint

// Hilt integration: Android creates this component with a generated dependency graph.
@AndroidEntryPoint
// Runtime type: `MainActivity` owns the properties and methods declared in this class body.
class MainActivity : ComponentActivity() {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Function `onCreate`: normal synchronous execution is used unless the body starts a coroutine itself; `override` replaces an implementation defined by a parent class/interface.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RynzoDriverTheme {
                RootNavGraph()
            }
        }

    }
}
