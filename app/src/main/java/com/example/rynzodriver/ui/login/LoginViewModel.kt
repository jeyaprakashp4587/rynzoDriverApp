package com.example.rynzodriver.ui.login

// Senior explanation:
// Login form state, credential submission, and authentication UI feedback.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements login UI and login ViewModel behavior.

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// AndroidX imports: navigation or lifecycle infrastructure used by this file.
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.domain.usecase.LoginUseCase
import com.example.rynzodriver.util.ToastService
// Hilt imports: dependency injection annotations and ViewModel integration.
import dagger.hilt.android.lifecycle.HiltViewModel
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Hilt integration: the framework constructs this ViewModel and injects annotated dependencies.
@HiltViewModel
// Runtime type: `LoginViewModel` owns the properties and methods declared in this class body.
class LoginViewModel @Inject constructor(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val loginUseCase: LoginUseCase,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val toastService: ToastService
) : ViewModel() {

// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
    private val _uiState = MutableStateFlow(LoginUiState())
// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Read-only state exposure: callers can collect but should not mutate this flow; mutation remains inside the owning class.
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

// Function `submitCredentials`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun submitCredentials(mobile: String, pass: String) {
// `if` guard: the following block executes only while `mobile.isEmpty() || pass.isEmpty()` evaluates to `true`; a false result skips it.
        if (mobile.isEmpty() || pass.isEmpty()) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val errorMsg = "Please enter mobile and password"
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
            _uiState.update { it.copy(error = errorMsg) }
            toastService.showToast(errorMsg)
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
            return
        }
        
// Coroutine lifecycle: `viewModelScope` starts work tied to this ViewModel and cancels it before a ViewModel leak can occur.
        viewModelScope.launch {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
            _uiState.update { it.copy(isLoading = true, error = null) }
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val result = loginUseCase(mobile, pass)
            result.onSuccess {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                toastService.showToast("Login Successful")
            }.onFailure { exception ->
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                val errorMsg = exception.message ?: "Login failed"
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                _uiState.update { it.copy(isLoading = false, error = errorMsg) }
                toastService.showToast(errorMsg)
            }
        }
    }

// Function `resetState`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun resetState() {
        _uiState.value = LoginUiState()
    }
}

// Data model: `LoginUiState` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class LoginUiState(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isLoading: Boolean = false,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isSuccess: Boolean = false,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val error: String? = null
)
