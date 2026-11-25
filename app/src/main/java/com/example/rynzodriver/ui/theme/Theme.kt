package com.example.rynzodriver.ui.theme

// Senior explanation:
// Application colors, typography, and Material 3 theme behavior.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file defines the application theme, colors, or typography.

// Android imports: platform classes and services; these APIs interact with the Android system.
import android.app.Activity
import android.os.Build
// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
private val DarkColorScheme = darkColorScheme(
    primary = UberWhite,
    onPrimary = UberBlack,
    secondary = UberBlue,
    onSecondary = UberWhite,
    background = UberBlack,
    onBackground = UberWhite,
    surface = UberBlack,
    onSurface = UberWhite
)

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
private val LightColorScheme = lightColorScheme(
    primary = UberBlack,
    onPrimary = UberWhite,
    secondary = UberBlue,
    onSecondary = UberWhite,
    background = UberWhite,
    onBackground = UberBlack,
    surface = UberWhite,
    onSurface = UberBlack,
    surfaceVariant = UberGrey
)

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `RynzoDriverTheme`: normal synchronous execution is used unless the body starts a coroutine itself.
fun RynzoDriverTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val context = LocalContext.current
// `if` guard: the following block executes only while `darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context` evaluates to `true`; a false result skips it.
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
// `else` fallback: this block runs only when the preceding `if` condition did not match.
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
