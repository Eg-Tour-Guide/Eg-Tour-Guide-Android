package com.egtourguide.auth.presentation.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.auth.domain.usecases.LoginUseCase
import com.egtourguide.core.domain.validation.Validation
import com.egtourguide.core.domain.validation.ValidationCases
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

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearNetworkError() {
        _uiState.update { it.copy(isNetworkError = false) }
    }

    fun onLoginClicked() {
        _uiState.update {
            it.copy(
                email = _uiState.value.email.trim(),
                emailError = ValidationCases.CORRECT,
                passwordError = ValidationCases.CORRECT
            )
        }

        if (checkData()) {
            login()
        }
    }

    private fun checkData(): Boolean {
        val emailErrorState = Validation.validateEmail(_uiState.value.email)
        val passwordErrorState = Validation.validatePassword(_uiState.value.password)

        _uiState.update {
            it.copy(
                emailError = emailErrorState,
                passwordError = passwordErrorState
            )
        }

        return emailErrorState == ValidationCases.CORRECT &&
                passwordErrorState == ValidationCases.CORRECT
    }

    fun login() {
        viewModelScope.launch(Dispatchers.IO) {
            loginUseCase(
                email = _uiState.value.email,
                password = _uiState.value.password
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                },
                onSuccess = { response ->
                    saveData(response.token)
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                },
                onFailure = { msg ->
                    _uiState.update { it.copy(isLoading = false, error = msg) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    private fun saveData(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            saveInDataStoreUseCase(key = TOKEN_KEY, value = token)
            saveInDataStoreUseCase(key = IS_LOGGED_KEY, value = true)
        }
    }
}
