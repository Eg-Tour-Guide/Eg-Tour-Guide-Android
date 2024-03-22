package com.egtourguide.auth.presentation.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(SignUpUIState())
    val uiState = _uiState.asStateFlow()

    fun changeName(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun changeEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun changePhone(phone: String) {
        _uiState.update { it.copy(phone = phone) }
    }

    fun changePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun changeConfirmPassword(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    fun sendCode() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000L)
            _uiState.update { it.copy(isSuccess = true, isLoading = false) }
        }
    }
}