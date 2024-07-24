package com.egtourguide.auth.presentation.screens.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.auth.domain.usecases.SendCodeUseCase
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
        _uiState.update { it.copy(isError = false) }
    }

    fun clearNetworkError() {
        _uiState.update { it.copy(isNetworkError = false) }
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
        val nameErrorState = Validation.validateName(_uiState.value.name)
        val emailErrorState = Validation.validateEmail(_uiState.value.email)
        val phoneErrorState = Validation.validatePhone(_uiState.value.phone)
        val passwordErrorState = Validation.validatePassword(_uiState.value.password)
        val confirmPasswordErrorState = Validation.validateConfirmPassword(
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
                    _uiState.update { it.copy(isLoading = true, isError = false) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isSuccess = true,
                            code = response.code
                        )
                    }
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
}