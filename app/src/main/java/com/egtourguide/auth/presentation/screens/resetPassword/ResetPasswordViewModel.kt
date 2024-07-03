package com.egtourguide.auth.presentation.screens.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.auth.data.dto.body.ResetPasswordRequestBody
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

    fun resetPassword(code: String) {
        val requestBody = ResetPasswordRequestBody(password = uiState.value.password)

        viewModelScope.launch(Dispatchers.IO) {
            resetPasswordUseCase(code = code, requestBody = requestBody).onResponse(
                onLoading = {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                },
                onFailure = { msg ->
                    _uiState.update {
                        it.copy(isLoading = false, error = msg)
                    }
                },
                onSuccess = {
                    _uiState.update {
                        it.copy(isPasswordResetSuccess = true)
                    }
                }
            )
        }
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

    fun clearSuccess() {
        _uiState.update { it.copy(isPasswordResetSuccess = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}