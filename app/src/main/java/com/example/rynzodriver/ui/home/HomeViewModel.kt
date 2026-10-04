package com.example.rynzodriver.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rynzodriver.data.api.ApiService
import com.example.rynzodriver.data.dto.vehicles.DriverVehicleDto
import com.example.rynzodriver.data.local.DataStoreManager
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
        fetchDriverVehicles()
    }

    fun fetchDriverVehicles() {
        viewModelScope.launch {
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

    fun openVehicleSheet() {
        _uiState.update { it.copy(isVehicleSheetOpen = true) }
    }

    fun dismissVehicleSheet() {
        _uiState.update { it.copy(isVehicleSheetOpen = false) }
    }

    fun selectVehicle(vehicle: DriverVehicleDto) {
        _uiState.update {
            it.copy(
                selectedVehicleId = vehicle.id,
                isVehicleSheetOpen = false
            )
        }
    }

    fun setOnDuty(isOnDuty: Boolean) {
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
    val error: String? = null
)
