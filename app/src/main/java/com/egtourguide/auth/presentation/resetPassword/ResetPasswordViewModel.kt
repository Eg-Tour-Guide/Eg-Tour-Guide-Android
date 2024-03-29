package com.egtourguide.auth.presentation.resetPassword

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.R
import com.egtourguide.auth.data.dto.body.ResetPasswordRequestBody
import com.egtourguide.auth.domain.use_cases.ResetPasswordUseCase
import com.egtourguide.auth.domain.validation.AuthValidation
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

    fun resetPassword(
        code: String
    ) {
        val requestBody = ResetPasswordRequestBody(password = uiState.value.password)
        viewModelScope.launch(Dispatchers.IO) {
            resetPasswordUseCase(
                code = code,
                requestBody = requestBody
            ).onResponse(
                onLoading = {
                    _uiState.update {
                        it.copy(isLoading = true, passwordError = null)
                    }
                },
                onFailure = { msg ->
                    _uiState.update {
                        it.copy(isLoading = false, passwordError = msg)
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

    fun onResetClicked(
        context: Context,
        code: String
    ) {
        _uiState.update { it.copy(passwordError = null, confirmPasswordError = null) }
        if (AuthValidation.validatePassword(password = uiState.value.password)) {
            if (AuthValidation
                    .validateConfirmPassword(
                        password = uiState.value.password,
                        confirmPassword = uiState.value.confirmPassword
                    )
            ) {
                resetPassword(code = code)
            } else {
                _uiState.update {
                    it.copy(
                        confirmPasswordError = context.getString(R.string.confirm_password_error_msg)
                    )
                }
            }
        } else {
            _uiState.update {
                it.copy(passwordError = context.getString(R.string.password_error_msg))
            }
        }
    }

}