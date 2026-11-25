package com.example.rynzodriver.ui.main

// Senior explanation:
// Authenticated application shell, root state, and bottom-navigation container.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements the main application shell and authentication state.

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// AndroidX imports: navigation or lifecycle infrastructure used by this file.
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.local.DataStoreManager
// Hilt imports: dependency injection annotations and ViewModel integration.
import dagger.hilt.android.lifecycle.HiltViewModel
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

// Hilt integration: the framework constructs this ViewModel and injects annotated dependencies.
@HiltViewModel
// Runtime type: `MainViewModel` owns the properties and methods declared in this class body.
class MainViewModel @Inject constructor(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
    private val _authState = MutableStateFlow<AuthState>(AuthState.Loading)
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Read-only state exposure: callers can collect but should not mutate this flow; mutation remains inside the owning class.
    val authState: StateFlow<AuthState> = _authState

    init {
// Coroutine lifecycle: `viewModelScope` starts work tied to this ViewModel and cancels it before a ViewModel leak can occur.
        viewModelScope.launch {
// `if` guard: the following block executes only while `!dataStoreManager.hasValidSession()` evaluates to `true`; a false result skips it.
            if (!dataStoreManager.hasValidSession()) {
                dataStoreManager.clearAll()
            }

            dataStoreManager.isLoggedIn.collectLatest { isLoggedIn ->
                _authState.value = if (isLoggedIn) {
                    AuthState.Authenticated
                } else {
                    AuthState.Unauthenticated
                }
            }
        }
    }
}

// Restricted type hierarchy: `{` only permits implementations/types listed in this module, enabling exhaustive `when` handling.
sealed class AuthState {
// Singleton object: `Loading` has one process-wide instance; initialization occurs the first time it is accessed.
    object Loading : AuthState()
// Singleton object: `Authenticated` has one process-wide instance; initialization occurs the first time it is accessed.
    object Authenticated : AuthState()
// Singleton object: `Unauthenticated` has one process-wide instance; initialization occurs the first time it is accessed.
    object Unauthenticated : AuthState()
}
