package com.egtourguide.auth.presentation.screens.forgotPassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.auth.data.dto.body.ForgotPasswordRequestBody
import com.egtourguide.auth.domain.usecases.GetForgotPasswordCodeUseCase
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
class ForgotPasswordViewModel @Inject constructor(
    private val getForgotPasswordCodeUseCase: GetForgotPasswordCodeUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ForgotPasswordUIState())
    val uiState = _uiState.asStateFlow()

    fun onEmailChanged(email: String) {
        _uiState.update { it.copy(email = email) }
    }

    private fun getForgotPasswordCode() {
        val requestBody = ForgotPasswordRequestBody(email = uiState.value.email)
        viewModelScope.launch(Dispatchers.IO) {
            getForgotPasswordCodeUseCase(
                requestBody = requestBody
            ).onResponse(
                onLoading = {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                },
                onFailure = { errorMsg ->
                    _uiState.update {
                        it.copy(
                            error = errorMsg,
                            isLoading = false
                        )
                    }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            successMessage = response.message,
                            code = response.code,
                            isCodeSentSuccessfully = true,
                            isLoading = false
                        )
                    }
                }
            )
        }
    }

    fun onNextClicked() {
        _uiState.update {
            it.copy(emailError = ValidationCases.CORRECT)
        }
        if(checkData()){
            getForgotPasswordCode()
        }

    }

    private fun checkData(): Boolean {
        val emailErrorState = AuthValidation.validateEmail(_uiState.value.email)
        _uiState.update {
            it.copy(emailError = emailErrorState)
        }
        return emailErrorState == ValidationCases.CORRECT
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isCodeSentSuccessfully = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }

}