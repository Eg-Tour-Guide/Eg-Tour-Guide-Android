package com.egtourguide.auth.presentation.login

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.R
import com.egtourguide.auth.data.dto.body.LoginRequestBody
import com.egtourguide.auth.domain.use_cases.LoginUseCase
import com.egtourguide.auth.domain.validation.AuthValidation
import com.egtourguide.core.utils.onResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase
) : ViewModel() {
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

    fun clearError() {
        _uiState.update { it.copy(isError = false) }
    }

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            val requestBody = LoginRequestBody(
                email = _uiState.value.email,
                password = _uiState.value.password
            )

            loginUseCase(
                requestBody = requestBody
            ).onResponse(
                onLoading = {
                    _uiState.update {
                        it.copy(isLoading = true)
                    }
                },
                onFailure = { msg ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isError = true,
                            error = msg
                        )
                    }
                },
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true
                        )
                    }
                }
            )
        }
    }

    fun onLoginClicked(
        context: Context
    ) {
        _uiState.update {
            it.copy(
                email = _uiState.value.email.trim(),
                emailError = null,
                passwordError = null
            )
        }
        if (AuthValidation.validateEmail(email = _uiState.value.email)) {
            if (AuthValidation.validatePassword(password = _uiState.value.password)) {
                login()
            } else {
                _uiState.update { it.copy(passwordError = context.getString(R.string.password_error_msg)) }
            }
        } else {
            _uiState.update { it.copy(emailError = context.getString(R.string.email_error_msg)) }
        }

    }
}

