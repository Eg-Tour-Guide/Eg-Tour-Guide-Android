package com.egtourguide.user.presentation.changePassword

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
class ChangePasswordViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(ChangePasswordState())
    val uiState = _uiState.asStateFlow()

    fun onOldPasswordChanged(oldPassword: String) {
        _uiState.update { it.copy(oldPassword = oldPassword) }
    }

    fun onNewPasswordChanged(newPassword: String) {
        _uiState.update { it.copy(newPassword = newPassword) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun onSaveClicked() {
        // TODO: Implement!!
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isLoading = true) }
            delay(1000L)
            _uiState.update { it.copy(isLoading = false, isSuccess = true) }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(isError = false) }
    }
}