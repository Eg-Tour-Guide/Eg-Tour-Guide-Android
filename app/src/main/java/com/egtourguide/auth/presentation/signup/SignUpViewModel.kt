package com.egtourguide.auth.presentation.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.auth.domain.usecases.SendCodeUseCase
import com.egtourguide.auth.domain.validation.AuthValidation
import com.egtourguide.auth.domain.validation.ValidationCases
import com.egtourguide.core.utils.Constants.TAG
import com.egtourguide.core.utils.onResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val sendCodeUseCase: SendCodeUseCase
) : ViewModel() {

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

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun onRegisterClicked() {
        _uiState.update {
            it.copy(
                nameError = ValidationCases.CORRECT,
                emailError = ValidationCases.CORRECT,
                phoneError = ValidationCases.CORRECT,
                passwordError = ValidationCases.CORRECT,
                confirmPasswordError = ValidationCases.CORRECT
            )
        }

        if (checkData()) {
            sendCode()
        }
    }

    private fun checkData(): Boolean {
        val nameErrorState = AuthValidation.validateName(_uiState.value.name)
        val emailErrorState = AuthValidation.validateEmail2(_uiState.value.email)
        val phoneErrorState = AuthValidation.validatePhone(_uiState.value.phone)
        val passwordErrorState = AuthValidation.validatePassword2(_uiState.value.password)
        val confirmPasswordErrorState = AuthValidation.validateConfirmPassword2(
            _uiState.value.password,
            _uiState.value.confirmPassword
        )

        _uiState.update {
            it.copy(
                nameError = nameErrorState,
                emailError = emailErrorState,
                phoneError = phoneErrorState,
                passwordError = passwordErrorState,
                confirmPasswordError = confirmPasswordErrorState
            )
        }

        return nameErrorState == ValidationCases.CORRECT &&
                emailErrorState == ValidationCases.CORRECT &&
                phoneErrorState == ValidationCases.CORRECT &&
                passwordErrorState == ValidationCases.CORRECT &&
                confirmPasswordErrorState == ValidationCases.CORRECT
    }

    private fun sendCode() {
        viewModelScope.launch(Dispatchers.IO) {
            sendCodeUseCase(email = _uiState.value.email).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = { response ->
                    // TODO: Get sent code!!
                    Log.d(TAG, "sendCode: $response")
                    _uiState.update { it.copy(isSuccess = true, code = "") }
                },
                onFailure = { msg ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                }
            )
        }
    }
}