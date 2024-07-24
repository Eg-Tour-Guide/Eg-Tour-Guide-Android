package com.egtourguide.auth.presentation.screens.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.auth.domain.usecases.GetForgotPasswordCodeUseCase
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
class ForgotPasswordViewModel @Inject constructor(
    private val getForgotPasswordCodeUseCase: GetForgotPasswordCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUIState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    fun onNextClicked() {
        _uiState.update { it.copy(emailError = ValidationCases.CORRECT) }

        if (checkData()) {
            getForgotPasswordCode()
        }
    }

    private fun checkData(): Boolean {
        val emailErrorState = Validation.validateEmail(_uiState.value.email)
        _uiState.update { it.copy(emailError = emailErrorState) }
        return emailErrorState == ValidationCases.CORRECT
    }

    private fun getForgotPasswordCode() {
        viewModelScope.launch(Dispatchers.IO) {
            getForgotPasswordCodeUseCase(email = uiState.value.email).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            successMessage = response.message,
                            code = response.code,
                            isLoading = false
                        )
                    }
                },
                onFailure = { errorMsg ->
                    _uiState.update { it.copy(error = errorMsg, isLoading = false) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

    fun clearNetworkError() {
        _uiState.update { it.copy(isNetworkError = false) }
    }
}