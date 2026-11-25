package com.example.rynzodriver.ui.home

// Senior explanation:
// Driver dashboard state, location monitoring, vehicle selection, and home navigation.
// Read top-down: imports provide dependencies, declarations define state/contracts, and the function body coordinates them.


// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// AndroidX imports: navigation or lifecycle infrastructure used by this file.
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
// Project imports: shared domain, data, UI, navigation, or utility types from this app.
import com.example.rynzodriver.data.api.ApiService
import com.example.rynzodriver.data.dto.vehicles.DriverVehicleDto
import com.example.rynzodriver.data.local.DataStoreManager
import com.example.rynzodriver.ui.companyTrips.CompanyTripUiModel
import com.example.rynzodriver.ui.companyTrips.toUiModel
// Hilt imports: dependency injection annotations and ViewModel integration.
import dagger.hilt.android.lifecycle.HiltViewModel
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Library imports: supporting types or annotations used by this declaration.
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

// Hilt integration: the framework constructs this ViewModel and injects annotated dependencies.
@HiltViewModel
// Runtime type: `HomeViewModel` owns the properties and methods declared in this class body.
class HomeViewModel @Inject constructor(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val apiService: ApiService,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Mutable observable state: `MutableStateFlow` owns the latest value and emits updates to collectors.
    private val _uiState = MutableStateFlow(HomeUiState())
// Reactive state: this value owns a latest StateFlow value and emits updates to active collectors.
// Read-only state exposure: callers can collect but should not mutate this flow; mutation remains inside the owning class.
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

// Function `consumeNavigationEvent`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun consumeNavigationEvent() {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
        _uiState.update { it.copy(navigationEvent = null) }
    }

// Function `loadHomeData`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun loadHomeData() {
// Coroutine lifecycle: `viewModelScope` starts work tied to this ViewModel and cancels it before a ViewModel leak can occur.
        viewModelScope.launch {
            fetchDriverVehicles()
        }
    }

// Function `fetchDriverTripStatus`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun fetchDriverTripStatus() {
// Coroutine lifecycle: `viewModelScope` starts work tied to this ViewModel and cancels it before a ViewModel leak can occur.
        viewModelScope.launch {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// `try` boundary: execution continues normally for success; any thrown exception may be captured by a matching `catch`.
// Exception boundary: operations here may throw; success continues normally and matching `catch` blocks handle failures.
            try {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val activeTripResponse = apiService.getDriverActiveTrip()
// `if` guard: the following block executes only while `activeTripResponse.isSuccessful && activeTripResponse.body()?.data != null` evaluates to `true`; a false result skips it.
                if (activeTripResponse.isSuccessful && activeTripResponse.body()?.data != null) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Explicit non-null assertion: `!!` tells Kotlin the value cannot be null; null at runtime throws an exception instead.
                    val activeData = activeTripResponse.body()!!.data!!
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
                    val isTripActive = activeData.isTripActive && !activeData.activeTripId.isNullOrBlank()

// `if` guard: the following block executes only while `isTripActive` evaluates to `true`; a false result skips it.
                    if (isTripActive) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Explicit non-null assertion: `!!` tells Kotlin the value cannot be null; null at runtime throws an exception instead.
                        val activeTripId = activeData.activeTripId!!
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                        val activeTripType = (activeData.activeTripType ?: "").trim().uppercase()
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                        val isCompanyTrip = activeTripType == "COMPANY_TRIP" || activeTripType == "COMPANY"

// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                        _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                            it.copy(
                                driverTripStatus = "ON_TRIP",
                                isTripActive = true,
                                activeTripId = activeTripId,
                                activeTripType = activeTripType,
                                isLoading = false,
                                companyTripsError = "Trip in progress. Vehicle selection and location tracking are locked."
                            )
                        }

// `if` guard: the following block executes only while `isCompanyTrip` evaluates to `true`; a false result skips it.
                        if (isCompanyTrip) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                            val companyTripUiModel = activeData.companyTrip?.toUiModel()
                                ?: CompanyTripUiModel(
                                    id = activeTripId,
                                    routeId = activeTripId,
                                    vehicleId = activeData.vehicleId.orEmpty(),
                                    driverId = activeData.driverId.orEmpty(),
                                    title = "Active Company Trip",
                                    vehicleLabel = "Vehicle",
                                    stops = listOf("Active Route")
                                )
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                            _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                                it.copy(navigationEvent = HomeNavigationEvent.NavigateToCompanyTrip(companyTripUiModel))
                            }
                        } else {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                            _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                                it.copy(navigationEvent = HomeNavigationEvent.NavigateToGenericTrip(activeTripId))
                            }
                        }
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                        return@launch
                    } else {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                        _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                            it.copy(
                                driverTripStatus = "IDLE",
                                isTripActive = false,
                                activeTripId = null,
                                activeTripType = null,
                                companyTripsError = null
                            )
                        }
                    }
                } else {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                    _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                        it.copy(
                            driverTripStatus = "IDLE",
                            isTripActive = false,
                            activeTripId = null,
                            activeTripType = null
                        )
                    }
                }
// Failure recovery: this catch block receives the thrown exception and can update state, notify the UI, or rethrow.
            } catch (e: Exception) {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    it.copy(
                        driverTripStatus = "IDLE",
                        isTripActive = false
                    )
                }
            }
        }
    }

// Function `fetchDriverVehicles`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun fetchDriverVehicles() {
// Coroutine lifecycle: `viewModelScope` starts work tied to this ViewModel and cancels it before a ViewModel leak can occur.
        viewModelScope.launch {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
            _uiState.update { it.copy(isLoading = true, error = null) }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val userId = dataStoreManager.userId.first()
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
// `if` guard: the following block executes only while `userId.isNullOrBlank()` evaluates to `true`; a false result skips it.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
            if (userId.isNullOrBlank()) {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    it.copy(
                        isLoading = false,
                        error = "User not found. Please login again."
                    )
                }
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                return@launch
            }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// `try` boundary: execution continues normally for success; any thrown exception may be captured by a matching `catch`.
// Exception boundary: operations here may throw; success continues normally and matching `catch` blocks handle failures.
            try {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val response = apiService.getDriverVehicles(userId)
// `if` guard: the following block executes only while `response.isSuccessful` evaluates to `true`; a false result skips it.
                if (response.isSuccessful) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                    val vehicles = response.body()?.data.orEmpty()
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                    val selectedVehicleId = _uiState.value.selectedVehicleId
// Safe search: `firstOrNull` returns the first matching item or null when no item satisfies the condition.
                        ?: vehicles.firstOrNull()?.id

// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                    _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                        it.copy(
                            isLoading = false,
                            vehicles = vehicles,
                            selectedVehicleId = selectedVehicleId,
                            error = null
                        )
                    }

                    // Check active trip for this driver
                    fetchDriverTripStatus()

// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
// `if` guard: the following block executes only while `!selectedVehicleId.isNullOrBlank()` evaluates to `true`; a false result skips it.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
                    if (!selectedVehicleId.isNullOrBlank()) {
                        fetchCompanyTripsForVehicle(selectedVehicleId)
                    } else {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                        _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                            it.copy(
                                isCompanyTripsLoading = false,
                                companyTrips = emptyList(),
                                companyTripsError = "Please select a vehicle to view company trips"
                            )
                        }
                    }
                } else {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                    val message = response.errorBody()?.string() ?: "Failed to load vehicles"
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                    _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                        it.copy(
                            isLoading = false,
                            error = message
                        )
                    }
                }
// Failure recovery: this catch block receives the thrown exception and can update state, notify the UI, or rethrow.
            } catch (e: Exception) {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    it.copy(
                        isLoading = false,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                        error = e.message ?: "Something went wrong while loading vehicles"
                    )
                }
            }
        }
    }

// Function `fetchCompanyTripsForVehicle`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun fetchCompanyTripsForVehicle(vehicleId: String?) {
// Coroutine lifecycle: `viewModelScope` starts work tied to this ViewModel and cancels it before a ViewModel leak can occur.
        viewModelScope.launch {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
            val userId = dataStoreManager.userId.first()
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
// `if` guard: the following block executes only while `userId.isNullOrBlank()` evaluates to `true`; a false result skips it.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
            if (userId.isNullOrBlank()) {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    it.copy(
                        isCompanyTripsLoading = false,
                        companyTripsError = "User not found. Please login again."
                    )
                }
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                return@launch
            }

// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
// `if` guard: the following block executes only while `vehicleId.isNullOrBlank()` evaluates to `true`; a false result skips it.
// Validation: `isNullOrBlank()` is true for null, an empty string, or a string containing only whitespace.
            if (vehicleId.isNullOrBlank()) {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    it.copy(
                        isCompanyTripsLoading = false,
                        companyTrips = emptyList(),
                        companyTripsError = "Select a vehicle to load company trips"
                    )
                }
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
// Labeled return: `return@LaunchedEffect` exits only the supplied effect lambda, not the surrounding Composable function.
                return@launch
            }

// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
            _uiState.update { it.copy(isCompanyTripsLoading = true, companyTripsError = null) }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// `try` boundary: execution continues normally for success; any thrown exception may be captured by a matching `catch`.
// Exception boundary: operations here may throw; success continues normally and matching `catch` blocks handle failures.
            try {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
                val response = apiService.getDriverCompanyRoutes(userId, vehicleId)
// `if` guard: the following block executes only while `response.isSuccessful` evaluates to `true`; a false result skips it.
                if (response.isSuccessful) {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Collection transformation: `map` returns a new list with one transformed result per item; `mapIndexed` also supplies each index.
                    val trips = response.body()?.data.orEmpty().map { it.toUiModel() }
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                    _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                        it.copy(
                            isCompanyTripsLoading = false,
                            companyTrips = trips,
                            companyTripsError = null
                        )
                    }
                } else {
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                    val message = response.errorBody()?.string() ?: "Failed to load company trips"
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                    _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                        it.copy(
                            isCompanyTripsLoading = false,
                            companyTripsError = message
                        )
                    }
                }
// Failure recovery: this catch block receives the thrown exception and can update state, notify the UI, or rethrow.
            } catch (e: Exception) {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
                _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                    it.copy(
                        isCompanyTripsLoading = false,
// Elvis fallback: `?:` evaluates the left side; if it is null, Kotlin evaluates and returns the right-side value.
                        companyTripsError = e.message ?: "Something went wrong while loading company trips"
                    )
                }
            }
        }
    }

// Function `openVehicleSheet`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun openVehicleSheet() {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
        _uiState.update { it.copy(isVehicleSheetOpen = true) }
    }

// Function `dismissVehicleSheet`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun dismissVehicleSheet() {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
        _uiState.update { it.copy(isVehicleSheetOpen = false) }
    }

// Function `selectVehicle`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun selectVehicle(vehicle: DriverVehicleDto) {
// `if` guard: the following block executes only while `_uiState.value.isTripActive` evaluates to `true`; a false result skips it.
        if (_uiState.value.isTripActive) {
// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
            _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
                it.copy(
                    isVehicleSheetOpen = false,
                    companyTripsError = "Trip in progress. You cannot change the vehicle while on trip."
                )
            }
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
            return
        }

// Atomic state update: `update` reads the latest state and replaces it with the value returned by the lambda.
        _uiState.update {
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
            it.copy(
                selectedVehicleId = vehicle.id,
                isVehicleSheetOpen = false,
                companyTripsError = null
            )
        }
        fetchCompanyTripsForVehicle(vehicle.id)
    }

// Function `setOnDuty`: normal synchronous execution is used unless the body starts a coroutine itself.
    fun setOnDuty(isOnDuty: Boolean) {
// `if` guard: the following block executes only while `_uiState.value.isTripActive` evaluates to `true`; a false result skips it.
        if (_uiState.value.isTripActive) {
// `return` control: execution leaves this function immediately; a labeled return can leave a lambda instead.
            return
        }
// Immutable update: `copy` creates a new data-class instance while replacing only the named properties.
        _uiState.update { it.copy(isOnDuty = isOnDuty) }
    }

// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val selectedVehicle: DriverVehicleDto?
// Safe search: `firstOrNull` returns the first matching item or null when no item satisfies the condition.
        get() = _uiState.value.vehicles.firstOrNull { it.id == _uiState.value.selectedVehicleId }
// Safe search: `firstOrNull` returns the first matching item or null when no item satisfies the condition.
            ?: _uiState.value.vehicles.firstOrNull()
}

// Restricted type hierarchy: `{` only permits implementations/types listed in this module, enabling exhaustive `when` handling.
sealed class HomeNavigationEvent {
// Data model: `NavigateToCompanyTrip` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
    data class NavigateToCompanyTrip(val trip: CompanyTripUiModel) : HomeNavigationEvent()
// Data model: `NavigateToGenericTrip` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
    data class NavigateToGenericTrip(val tripId: String) : HomeNavigationEvent()
}

// Data model: `HomeUiState` automatically provides `toString`, `copy`, equality, and hash behavior for these immutable constructor properties.
data class HomeUiState(
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isLoading: Boolean = false,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val vehicles: List<DriverVehicleDto> = emptyList(),
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val selectedVehicleId: String? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isVehicleSheetOpen: Boolean = false,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isOnDuty: Boolean = false,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val driverTripStatus: String = "IDLE",
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isTripActive: Boolean = false,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val activeTripId: String? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val activeTripType: String? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val navigationEvent: HomeNavigationEvent? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val companyTrips: List<CompanyTripUiModel> = emptyList(),
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val isCompanyTripsLoading: Boolean = false,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val companyTripsError: String? = null,
// Delegation: `by` forwards property get/set behavior to the object on the right instead of storing the value directly here.
// Read-only property: `val` binds the value once. Later reassignment of this property name is prevented by Kotlin.
    val error: String? = null
)
