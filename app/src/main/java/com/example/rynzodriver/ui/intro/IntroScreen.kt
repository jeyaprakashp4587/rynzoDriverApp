package com.example.rynzodriver.ui.intro

// Senior explanation:
// Initial welcome presentation and continuation action.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements the introductory welcome screen.

// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `IntroScreen`: normal synchronous execution is used unless the body starts a coroutine itself.
fun IntroScreen(onContinue: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                modifier = Modifier.padding(top = 80.dp)
            ) {
                Text(
                    text = "RYNZO",
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    style = MaterialTheme.typography.displayMedium.copy(
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 4.sp
                    ),
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Logistics Redefined.",
                    style = MaterialTheme.typography.headlineSmall,
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f)
                )
            }

            Column(
                modifier = Modifier.padding(bottom = 32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Drive with Rynzo. Move the world.",
                    style = MaterialTheme.typography.bodyLarge,
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                    modifier = Modifier.padding(bottom = 48.dp)
                )

                Button(
                    onClick = onContinue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    )
                ) {
                    Text(
                        text = "Get Started",
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }
        }
    }
}
