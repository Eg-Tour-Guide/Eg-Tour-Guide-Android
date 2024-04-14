package com.egtourguide.auth.presentation.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.auth.domain.usecases.SignupUseCase
import com.egtourguide.auth.domain.validation.AuthValidation
import com.egtourguide.auth.domain.validation.ValidationCases
import com.egtourguide.core.utils.onResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class OtpViewModel @Inject constructor(
    private val signupUseCase: SignupUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OtpUIState())
    val uiState = _uiState.asStateFlow()

    fun changeCode(code: String) {
        _uiState.update { it.copy(code = code) }
    }

    fun clearVerifySuccess() {
        _uiState.update { it.copy(isVerifiedSuccessfully = false) }
    }

    fun clearSignSuccess() {
        _uiState.update { it.copy(isSignedSuccessfully = false) }
    }

    fun onVerifyClicked(sentCode: String) {
        if (checkCode(sentCode = sentCode)) {
            _uiState.update { it.copy(isVerifiedSuccessfully = true) }
        }
    }

    private fun checkCode(sentCode: String): Boolean {
        _uiState.update { it.copy(codeError = ValidationCases.CORRECT) }

        val isCodeCorrect =
            AuthValidation.validateCode(sentCode = sentCode, code = _uiState.value.code)
        _uiState.update { it.copy(codeError = isCodeCorrect) }
        return isCodeCorrect == ValidationCases.CORRECT
    }

    fun signup(
        name: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            signupUseCase(
                name = name,
                email = email,
                phone = phone,
                password = password,
                confirmPassword = confirmPassword
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = { response ->
                    // TODO: Save token!!
                    _uiState.update { it.copy(isLoading = false, isSignedSuccessfully = true) }
                },
                onFailure = { msg ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                }
            )
        }
    }
}