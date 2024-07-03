package com.egtourguide.user.presentation.changePassword

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
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
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isSuccess = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}