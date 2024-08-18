package com.egtourguide.auth.presentation.screens.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.auth.domain.model.SignupResponse
import com.egtourguide.auth.domain.usecases.GetForgotPasswordCodeUseCase
import com.egtourguide.auth.domain.usecases.SendCodeUseCase
import com.egtourguide.auth.domain.usecases.SignupUseCase
import com.egtourguide.core.domain.validation.Validation
import com.egtourguide.core.domain.validation.ValidationCases
import com.egtourguide.core.domain.usecases.SaveInDataStoreUseCase
import com.egtourguide.core.utils.DataStoreKeys
import com.egtourguide.core.utils.DataStoreKeys.USER_EMAIL_KEY
import com.egtourguide.core.utils.DataStoreKeys.USER_NAME_KEY
import com.egtourguide.core.utils.DataStoreKeys.USER_PHONE_KEY
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
    private val signupUseCase: SignupUseCase,
    private val saveInDataStoreUseCase: SaveInDataStoreUseCase,
    private val sendCodeUseCase: SendCodeUseCase,
    private val forgotPasswordCodeUseCase: GetForgotPasswordCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(OtpUIState())
    val uiState = _uiState.asStateFlow()

    fun changeCode(code: String) {
        _uiState.update { it.copy(code = code) }
    }

    fun clearVerifySuccess() {
        _uiState.update { it.copy(isVerifiedSuccessfully = false) }
    }

    fun resendCode(fromSignup: Boolean) {
        if (fromSignup) resendSignupCode()
        else resendForgotPasswordCode()
    }

    private fun resendForgotPasswordCode() {
        viewModelScope.launch(Dispatchers.IO) {
            forgotPasswordCodeUseCase(email = _uiState.value.email).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = { response ->
                    _uiState.update { it.copy(isLoading = false, sentCode = response.code) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    private fun resendSignupCode() {
        viewModelScope.launch(Dispatchers.IO) {
            sendCodeUseCase(email = _uiState.value.email).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = { response ->
                    _uiState.update { it.copy(isLoading = false, sentCode = response.code) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    fun onVerifyClicked() {
        if (checkCode()) {
            _uiState.update { it.copy(isVerifiedSuccessfully = true) }
        }
    }

    private fun checkCode(): Boolean {
        _uiState.update { it.copy(codeError = ValidationCases.CORRECT) }

        val isCodeCorrect = Validation.validateCode(
            sentCode = _uiState.value.sentCode,
            code = _uiState.value.code
        )

        _uiState.update { it.copy(codeError = isCodeCorrect) }

        return isCodeCorrect == ValidationCases.CORRECT
    }

    fun saveData(
        name: String,
        email: String,
        phone: String,
        password: String,
        sentCode: String
    ) {
        _uiState.update {
            it.copy(
                name = name,
                email = email,
                phone = phone,
                password = password,
                sentCode = sentCode
            )
        }
    }

    fun signup() {
        viewModelScope.launch(Dispatchers.IO) {
            signupUseCase(
                name = _uiState.value.name,
                email = _uiState.value.email,
                phone = _uiState.value.phone,
                password = _uiState.value.password
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = { response ->
                    saveData(response = response)
                    _uiState.update { it.copy(isLoading = false, isSignedSuccessfully = true) }
                },
                onFailure = { msg ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = msg) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    private fun saveData(response: SignupResponse) {
        viewModelScope.launch(Dispatchers.IO) {
            saveInDataStoreUseCase(key = DataStoreKeys.TOKEN_KEY, value = response.token)
            saveInDataStoreUseCase(key = DataStoreKeys.IS_LOGGED_KEY, value = true)
            saveInDataStoreUseCase(key = USER_NAME_KEY, value = response.userName)
            saveInDataStoreUseCase(key = USER_EMAIL_KEY, value = response.email)
            saveInDataStoreUseCase(key = USER_PHONE_KEY, value = response.phone)
        }
    }

    fun clearNetworkError() {
        _uiState.update { it.copy(isNetworkError = false) }
    }
}