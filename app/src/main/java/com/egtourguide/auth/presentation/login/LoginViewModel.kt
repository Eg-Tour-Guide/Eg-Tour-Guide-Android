package com.egtourguide.auth.presentation.login

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
class LoginViewModel @Inject constructor() : ViewModel() {
    private val _uiState = MutableStateFlow(LoginState())
    val uiState = _uiState.asStateFlow()

    fun changeEmail(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun changePassword(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    fun loginClick() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000L)
            _uiState.update { it.copy(isSuccess = true, isLoading = false) }
        }
    }
}

