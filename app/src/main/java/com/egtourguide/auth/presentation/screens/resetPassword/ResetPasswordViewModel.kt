package com.egtourguide.auth.presentation.screens.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.auth.domain.usecases.ResetPasswordUseCase
import com.egtourguide.core.domain.validation.Validation
import com.egtourguide.core.domain.validation.ValidationCases
import com.egtourguide.core.utils.onResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ResetPasswordViewModel @Inject constructor(
    private val resetPasswordUseCase: ResetPasswordUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResetPasswordUIState())
    val uiState = _uiState.asStateFlow()

    fun onPasswordChanged(password: String) {
        _uiState.update { it.copy(password = password) }
    }

    fun onConfirmPasswordChanged(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword) }
    }

    fun onResetClicked(code: String) {
        _uiState.update {
            it.copy(
                passwordError = ValidationCases.CORRECT,
                confirmPasswordError = ValidationCases.CORRECT
            )
        }

        if (checkData()) {
            resetPassword(code = code)
        }
    }

    private fun checkData(): Boolean {
        val passwordErrorState = Validation.validatePassword(_uiState.value.password)
        val confirmPasswordErrorState = Validation.validateConfirmPassword(
            _uiState.value.password,
            _uiState.value.confirmPassword
        )

        _uiState.update {
            it.copy(
                passwordError = passwordErrorState,
                confirmPasswordError = confirmPasswordErrorState
            )
        }

        return passwordErrorState == ValidationCases.CORRECT && confirmPasswordErrorState == ValidationCases.CORRECT
    }

    fun resetPassword(code: String) {
        viewModelScope.launch(Dispatchers.IO) {
            resetPasswordUseCase(code = code, password = _uiState.value.password).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, isError = false) }
                },
                onSuccess = {
                    _uiState.update { it.copy(isPasswordResetSuccess = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false, isError = true) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(isError = false) }
    }

    fun clearNetworkError() {
        _uiState.update { it.copy(isNetworkError = false) }
    }
}