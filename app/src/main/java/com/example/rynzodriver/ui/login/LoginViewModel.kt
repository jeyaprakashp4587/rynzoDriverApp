package com.example.rynzodriver.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rynzodriver.domain.usecase.LoginUseCase
import com.example.rynzodriver.util.ToastService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val toastService: ToastService
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun submitCredentials(mobile: String, pass: String) {
        if (mobile.isEmpty() || pass.isEmpty()) {
            val errorMsg = "Please enter mobile and password"
            _uiState.update { it.copy(error = errorMsg) }
            toastService.showToast(errorMsg)
            return
        }
        
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = loginUseCase(mobile, pass)
            result.onSuccess {
                _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                toastService.showToast("Login Successful")
            }.onFailure { exception ->
                val errorMsg = exception.message ?: "Login failed"
                _uiState.update { it.copy(isLoading = false, error = errorMsg) }
                toastService.showToast(errorMsg)
            }
        }
    }

    fun resetState() {
        _uiState.value = LoginUiState()
    }
}

data class LoginUiState(
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null
)
