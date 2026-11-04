package com.example.rynzodriver.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rynzodriver.data.api.ApiService
import com.example.rynzodriver.data.dto.vehicles.DriverVehicleDto
import com.example.rynzodriver.data.local.DataStoreManager
import com.example.rynzodriver.ui.companyTrips.CompanyTripUiModel
import com.example.rynzodriver.ui.companyTrips.toUiModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val apiService: ApiService,
    private val dataStoreManager: DataStoreManager
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        fetchDriverTripStatus()
        fetchDriverVehicles()
    }

    fun fetchDriverTripStatus() {
        viewModelScope.launch {
            try {
                val response = apiService.getDriverProfile()
                if (response.isSuccessful) {
                    val tripStatus = response.body()?.data?.tripStatus?.trim()?.uppercase()
                    val isTripActive = tripStatus == "ON_TRIP"

                    _uiState.update {
                        it.copy(
                            driverTripStatus = tripStatus ?: "IDLE",
                            isTripActive = isTripActive,
                            companyTripsError = if (isTripActive) {
                                "Trip in progress. Vehicle selection and location tracking are locked."
                            } else it.companyTripsError
                        )
                    }

                    if (isTripActive) {
                        _uiState.update {
                            it.copy(
                                isLoading = false,
                                selectedVehicleId = null,
                                vehicles = emptyList(),
                                companyTrips = emptyList()
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        driverTripStatus = "IDLE",
                        isTripActive = false
                    )
                }
            }
        }
    }

    fun fetchDriverVehicles() {
        viewModelScope.launch {
            if (_uiState.value.isTripActive) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        vehicles = emptyList(),
                        selectedVehicleId = null,
                        companyTrips = emptyList(),
                        companyTripsError = "Trip in progress. Vehicle selection is locked."
                    )
                }
                return@launch
            }

            _uiState.update { it.copy(isLoading = true, error = null) }

            val userId = dataStoreManager.userId.first()
            if (userId.isNullOrBlank()) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = "User not found. Please login again."
                    )
                }
                return@launch
            }

            try {
                val response = apiService.getDriverVehicles(userId)
                if (response.isSuccessful) {
                    val vehicles = response.body()?.data.orEmpty()
                    val selectedVehicleId = if (vehicles.isNotEmpty()) {
                        vehicles.first().id
                    } else {
                        null
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            vehicles = vehicles,
                            selectedVehicleId = selectedVehicleId,
                            error = null
                        )
                    }

                    if (!selectedVehicleId.isNullOrBlank()) {
                        fetchCompanyTripsForVehicle(selectedVehicleId)
                    } else {
                        _uiState.update {
                            it.copy(
                                isCompanyTripsLoading = false,
                                companyTrips = emptyList(),
                                companyTripsError = "Please select a vehicle to view company trips"
                            )
                        }
                    }
                } else {
                    val message = response.errorBody()?.string() ?: "Failed to load vehicles"
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = message
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "Something went wrong while loading vehicles"
                    )
                }
            }
        }
    }

    fun fetchCompanyTripsForVehicle(vehicleId: String?) {
        viewModelScope.launch {
            val userId = dataStoreManager.userId.first()
            if (userId.isNullOrBlank()) {
                _uiState.update {
                    it.copy(
                        isCompanyTripsLoading = false,
                        companyTripsError = "User not found. Please login again."
                    )
                }
                return@launch
            }

            if (vehicleId.isNullOrBlank()) {
                _uiState.update {
                    it.copy(
                        isCompanyTripsLoading = false,
                        companyTrips = emptyList(),
                        companyTripsError = "Select a vehicle to load company trips"
                    )
                }
                return@launch
            }

            _uiState.update { it.copy(isCompanyTripsLoading = true, companyTripsError = null) }

            try {
                val response = apiService.getDriverCompanyRoutes(userId, vehicleId)
                if (response.isSuccessful) {
                    val trips = response.body()?.data.orEmpty().map { it.toUiModel() }
                    _uiState.update {
                        it.copy(
                            isCompanyTripsLoading = false,
                            companyTrips = trips,
                            companyTripsError = null
                        )
                    }
                } else {
                    val message = response.errorBody()?.string() ?: "Failed to load company trips"
                    _uiState.update {
                        it.copy(
                            isCompanyTripsLoading = false,
                            companyTripsError = message
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isCompanyTripsLoading = false,
                        companyTripsError = e.message ?: "Something went wrong while loading company trips"
                    )
                }
            }
        }
    }

    fun openVehicleSheet() {
        _uiState.update { it.copy(isVehicleSheetOpen = true) }
    }

    fun dismissVehicleSheet() {
        _uiState.update { it.copy(isVehicleSheetOpen = false) }
    }

    fun selectVehicle(vehicle: DriverVehicleDto) {
        if (_uiState.value.isTripActive) {
            _uiState.update {
                it.copy(
                    companyTripsError = "Trip in progress. You cannot change the vehicle while on trip."
                )
            }
            return
        }

        _uiState.update {
            it.copy(
                selectedVehicleId = vehicle.id,
                isVehicleSheetOpen = false
            )
        }
        fetchCompanyTripsForVehicle(vehicle.id)
    }

    fun setOnDuty(isOnDuty: Boolean) {
        if (_uiState.value.isTripActive) {
            return
        }
        _uiState.update { it.copy(isOnDuty = isOnDuty) }
    }

    val selectedVehicle: DriverVehicleDto?
        get() = _uiState.value.vehicles.firstOrNull { it.id == _uiState.value.selectedVehicleId }
            ?: _uiState.value.vehicles.firstOrNull()
}

data class HomeUiState(
    val isLoading: Boolean = false,
    val vehicles: List<DriverVehicleDto> = emptyList(),
    val selectedVehicleId: String? = null,
    val isVehicleSheetOpen: Boolean = false,
    val isOnDuty: Boolean = false,
    val driverTripStatus: String = "IDLE",
    val isTripActive: Boolean = false,
    val companyTrips: List<CompanyTripUiModel> = emptyList(),
    val isCompanyTripsLoading: Boolean = false,
    val companyTripsError: String? = null,
    val error: String? = null
)
