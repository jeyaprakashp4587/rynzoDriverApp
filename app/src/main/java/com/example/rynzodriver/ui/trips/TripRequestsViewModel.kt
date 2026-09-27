package com.example.rynzodriver.ui.trips

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rynzodriver.domain.model.trips.Trip
import com.example.rynzodriver.domain.model.trips.TripDetail
import com.example.rynzodriver.domain.usecase.trips.GetTripDetailsUseCase
import com.example.rynzodriver.domain.usecase.trips.GetTripRequestsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TripRequestsViewModel @Inject constructor(
    private val getTripRequestsUseCase: GetTripRequestsUseCase,
    private val getTripDetailsUseCase: GetTripDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(TripRequestsUiState())
    val uiState: StateFlow<TripRequestsUiState> = _uiState.asStateFlow()

    init {
        fetchTripRequests()
    }

    fun fetchTripRequests() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = getTripRequestsUseCase()
            result.onSuccess { trips ->
                _uiState.update { it.copy(isLoading = false, trips = trips) }
            }.onFailure { exception ->
                _uiState.update { it.copy(isLoading = false, error = exception.message ?: "Failed to load trips") }
            }
        }
    }

    fun fetchTripDetails(tripId: String) {
        viewModelScope.launch {
            _uiState.update {
                it.copy(
                    isDetailLoading = true,
                    detailError = null,
                    selectedTripId = tripId,
                    selectedTripDetail = null
                )
            }
            val result = getTripDetailsUseCase(tripId)
            result.onSuccess { tripDetail ->
                _uiState.update {
                    it.copy(
                        isDetailLoading = false,
                        selectedTripDetail = tripDetail,
                        isDetailSheetOpen = true
                    )
                }
            }.onFailure { exception ->
                _uiState.update {
                    it.copy(
                        isDetailLoading = false,
                        detailError = exception.message ?: "Failed to load trip details",
                        isDetailSheetOpen = false
                    )
                }
            }
        }
    }

    fun dismissDetailSheet() {
        _uiState.update {
            it.copy(
                isDetailSheetOpen = false,
                selectedTripDetail = null,
                detailError = null,
                isDetailLoading = false
            )
        }
    }
}

data class TripRequestsUiState(
    val isLoading: Boolean = false,
    val trips: List<Trip> = emptyList(),
    val error: String? = null,
    val isDetailLoading: Boolean = false,
    val detailError: String? = null,
    val selectedTripId: String? = null,
    val selectedTripDetail: TripDetail? = null,
    val isDetailSheetOpen: Boolean = false
)
