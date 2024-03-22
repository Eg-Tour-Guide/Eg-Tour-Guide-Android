package com.egtourguide.auth.presentation.resetPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.auth.data.dto.body.ResetPasswordRequestBody
import com.egtourguide.auth.domain.repository.AuthRepository
import com.egtourguide.auth.presentation.forgotPassword.ForgotPasswordUIState
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
    private val authRepository: AuthRepository
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
            authRepository.resetPassword(
                code = code,
                requestBody = requestBody
            ).onResponse(
                onLoading = {
                    _uiState.update {
                        it.copy(isLoading = true, error = null)
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

}