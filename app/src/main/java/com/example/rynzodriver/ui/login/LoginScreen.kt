package com.example.rynzodriver.ui.login

// Senior explanation:
// Login form state, credential submission, and authentication UI feedback.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements login UI and login ViewModel behavior.

// Jetpack Compose imports: declarative UI containers, state, modifiers, colors, and widgets.
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Hilt imports: dependency injection annotations and ViewModel integration.
import androidx.hilt.navigation.compose.hiltViewModel

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Opt-in boundary: this declaration intentionally uses an API marked experimental by the library.
@OptIn(ExperimentalMaterial3Api::class)
// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `LoginScreen`: normal synchronous execution is used unless the body starts a coroutine itself.
fun LoginScreen(
    onLoginSuccess: () -> Unit,
// Default dependency: if the caller omits this parameter, Hilt creates or retrieves the ViewModel for this Compose destination.
    viewModel: LoginViewModel = hiltViewModel()
) {
// Mutable property: `var` permits reassignment. Any observer is responsible for reacting if this value changes.
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
    var mobile by remember { mutableStateOf("") }
// Mutable property: `var` permits reassignment. Any observer is responsible for reacting if this value changes.
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
    var password by remember { mutableStateOf("") }
    
// Delegated property: `by` delegates reading/writing to the expression to the right. The delegate owns storage and observation semantics.
// State bridge: `collectAsState()` observes the Flow and exposes its latest value as Compose state, so the UI recomposes on each emission.
    val uiState by viewModel.uiState.collectAsState()

// Side-effect lifecycle: this coroutine runs when the Composable enters or when any key in parentheses changes.
    LaunchedEffect(uiState.isSuccess) {
// `if` guard: the following block executes only while `uiState.isSuccess` evaluates to `true`; a false result skips it.
        if (uiState.isSuccess) {
            onLoginSuccess()
            viewModel.resetState()
        }
    }

    Scaffold(
        containerColor = Color.White,
        topBar = {
            TopAppBar(
                title = {},
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(20.dp))
            
            Text(
                text = "Welcome back",
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                style = MaterialTheme.typography.headlineLarge.copy(
                    fontWeight = FontWeight.Medium,
                    color = Color.Black,
                    fontSize = 28.sp
                )
            )

            Spacer(modifier = Modifier.height(32.dp))

            CredentialsSection(
                mobile = mobile,
                onMobileChange = { mobile = it },
                password = password,
                onPasswordChange = { password = it }
            )

// `if` guard: the following block executes only while `uiState.error != null` evaluates to `true`; a false result skips it.
            if (uiState.error != null) {
                Text(
// Explicit non-null assertion: `!!` tells Kotlin the value cannot be null; null at runtime throws an exception instead.
                    text = uiState.error!!,
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = { 
                    viewModel.submitCredentials(mobile, password)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(8.dp),
                enabled = !uiState.isLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White,
                    disabledContainerColor = Color.DarkGray
                ),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp)
            ) {
// `if` guard: the following block executes only while `uiState.isLoading` evaluates to `true`; a false result skips it.
                if (uiState.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp), 
                        color = Color.White,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(
                        text = "Log in", 
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 18.sp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `CredentialsSection`: normal synchronous execution is used unless the body starts a coroutine itself.
fun CredentialsSection(
    mobile: String,
    onMobileChange: (String) -> Unit,
    password: String,
    onPasswordChange: (String) -> Unit
) {
    Column {
        UberTextField(
            value = mobile,
            onValueChange = onMobileChange,
            label = "Mobile number",
            keyboardType = KeyboardType.Text
        )

        Spacer(modifier = Modifier.height(24.dp))

        UberTextField(
            value = password,
            onValueChange = onPasswordChange,
            label = "Password",
            visualTransformation = PasswordVisualTransformation(),
            keyboardType = KeyboardType.Password
        )
    }
}

// Compose entry: recomposition invokes this function to describe the current UI state.
@Composable
// Function `UberTextField`: normal synchronous execution is used unless the body starts a coroutine itself.
fun UberTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    visualTransformation: androidx.compose.ui.text.input.VisualTransformation = androidx.compose.ui.text.input.VisualTransformation.None,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    Column {
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            placeholder = { Text(text = label, color = Color.LightGray) },
            visualTransformation = visualTransformation,
// Compose modifier chain: layout flags are applied from left to right, producing the final size/padding/scroll behavior.
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            shape = RoundedCornerShape(8.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedContainerColor = Color(0xFFF3F3F3),
                unfocusedContainerColor = Color(0xFFF3F3F3),
                focusedBorderColor = Color.Transparent,
                unfocusedBorderColor = Color.Transparent,
                cursorColor = Color.Black,
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black
            ),
            singleLine = true
        )
    }
}
