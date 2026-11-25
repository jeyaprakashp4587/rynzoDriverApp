package com.example.rynzodriver.ui.trips

// Senior explanation:
// Requested-trip presentation, list/detail state, and trip acceptance flow.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.

// File purpose: This file implements trip requests, trip details, and their Compose UI.

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// AndroidX imports: navigation or lifecycle infrastructure used by this file.
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.domain.model.trips.Trip
import com.example.rynzodriver.domain.model.trips.TripDetail
import com.example.rynzodriver.domain.usecase.trips.AcceptTripRequestUseCase
import com.example.rynzodriver.domain.usecase.trips.GetTripDetailsUseCase
import com.example.rynzodriver.domain.usecase.trips.GetTripRequestsUseCase
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
// Runtime type: `TripRequestsViewModel` owns the properties and methods declared in this class body.
class TripRequestsViewModel @Inject constructor(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val getTripRequestsUseCase: GetTripRequestsUseCase,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val getTripDetailsUseCase: GetTripDetailsUseCase,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val acceptTripRequestUseCase: AcceptTripRequestUseCase,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val toastService: ToastService
) : ViewModel() {

// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
    private val _uiState = MutableStateFlow(TripRequestsUiState())
// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Read-only state exposure: callers can collect but should not mutate this flow; mutation remains inside the owning class.
    val uiState: StateFlow<TripRequestsUiState> = _uiState.asStateFlow()

    init {
        fetchTripRequests()
    }

// Function `fetchTripRequests`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun fetchTripRequests() {
// Coroutine lifecycle: `viewModelScope` starts work tied to this ViewModel and cancels it before a ViewModel leak can occur.
        viewModelScope.launch {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
            _uiState.update { it.copy(isLoading = true, error = null) }
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val result = getTripRequestsUseCase()
            result.onSuccess { trips ->
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                _uiState.update { it.copy(isLoading = false, trips = trips) }
            }.onFailure { exception ->
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                _uiState.update { it.copy(isLoading = false, error = exception.message ?: "Failed to load trips") }
            }
        }
    }

// Function `fetchTripDetails`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun fetchTripDetails(tripId: String) {
// Coroutine lifecycle: `viewModelScope` starts work tied to this ViewModel and cancels it before a ViewModel leak can occur.
        viewModelScope.launch {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
            _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                it.copy(
                    isDetailLoading = true,
                    detailError = null,
                    selectedTripId = tripId,
                    selectedTripDetail = null
                )
            }
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val result = getTripDetailsUseCase(tripId)
            result.onSuccess { tripDetail ->
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    it.copy(
                        isDetailLoading = false,
                        selectedTripDetail = tripDetail,
                        isDetailSheetOpen = true
                    )
                }
            }.onFailure { exception ->
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    it.copy(
                        isDetailLoading = false,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                        detailError = exception.message ?: "Failed to load trip details",
                        isDetailSheetOpen = false
                    )
                }
            }
        }
    }

// Function `acceptTripRequest`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun acceptTripRequest(tripId: String, onSuccess: (() -> Unit)? = null) {
// `if` guard: the following block executes only while `_uiState.value.isAccepting` evaluates to `true`; a false result skips it.
        if (_uiState.value.isAccepting) return

// Coroutine lifecycle: `viewModelScope` starts work tied to this ViewModel and cancels it before a ViewModel leak can occur.
        viewModelScope.launch {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
            _uiState.update { it.copy(isAccepting = true, detailError = null) }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val result = acceptTripRequestUseCase(tripId)
            result.onSuccess { message ->
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val toastMessage = message.ifBlank { "Trip request accepted successfully" }
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    it.copy(
                        isAccepting = false,
                        isDetailSheetOpen = false,
                        selectedTripDetail = null,
                        detailError = null
                    )
                }
                toastService.showToast(toastMessage)
                onSuccess?.invoke()
            }.onFailure { exception ->
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                val errorMessage = exception.message ?: "Failed to accept trip request"
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                _uiState.update { it.copy(isAccepting = false, detailError = errorMessage) }
                toastService.showToast(errorMessage)
            }
        }
    }

// Function `dismissDetailSheet`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun dismissDetailSheet() {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
        _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
            it.copy(
                isDetailSheetOpen = false,
                selectedTripDetail = null,
                detailError = null,
                isDetailLoading = false,
                isAccepting = false
            )
        }
    }
}

// Data model: `TripRequestsUiState` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class TripRequestsUiState(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isLoading: Boolean = false,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val trips: List<Trip> = emptyList(),
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val error: String? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isDetailLoading: Boolean = false,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val detailError: String? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val selectedTripId: String? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val selectedTripDetail: TripDetail? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isDetailSheetOpen: Boolean = false,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isAccepting: Boolean = false
)
