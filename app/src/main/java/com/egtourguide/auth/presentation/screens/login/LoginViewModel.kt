package com.egtourguide.auth.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.auth.data.dto.body.LoginRequestBody
import com.egtourguide.auth.domain.usecases.LoginUseCase
import com.egtourguide.auth.domain.validation.AuthValidation
import com.egtourguide.core.domain.usecases.SaveInDataStoreUseCase
import com.egtourguide.core.utils.DataStoreKeys.IS_LOGGED_KEY
import com.egtourguide.core.utils.DataStoreKeys.TOKEN_KEY
import com.egtourguide.core.utils.onResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val loginUseCase: LoginUseCase,
    private val saveInDataStoreUseCase: SaveInDataStoreUseCase
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
                        it.copy(isLoading = true, error = null)
                    }
                },
                onFailure = { msg ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            error = msg
                        )
                    }
                },
                onSuccess = { response ->
                    saveData(response.token)

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

    fun onLoginClicked() {
        _uiState.update {
            it.copy(
                email = _uiState.value.email.trim(),
                emailError = false,
                passwordError = false
            )
        }

        if (AuthValidation.validateEmail(email = _uiState.value.email)) {
            if (AuthValidation.validatePassword(password = _uiState.value.password)) {
                login()
            } else {
                _uiState.update { it.copy(passwordError = true) }
            }
        } else {
            _uiState.update { it.copy(emailError = true) }
        }
    }

    private fun saveData(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveInDataStoreUseCase(key = TOKEN_KEY, value = token)
            saveInDataStoreUseCase(key = IS_LOGGED_KEY, value = true)
        }
    }
}