package com.egtourguide.auth.presentation.resetPassword

import androidx.lifecycle.ViewModel
import com.egtourguide.auth.presentation.forgotPassword.ForgotPasswordUIState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(

) : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUIState())
    val uiState = _uiState.asStateFlow()

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun onResetPasswordClicked() {

    }

}