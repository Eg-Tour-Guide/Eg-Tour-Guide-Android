package com.egtourguide.auth.presentation.screens.forgotPassword

import com.egtourguide.auth.domain.validation.ValidationCases

data class ForgotPasswordUIState(
    val email: String = "",
    val emailError: ValidationCases = ValidationCases.CORRECT,
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCodeSentSuccessfully: Boolean = false,
    val successMessage: String? = null,
    val code: String = ""
)
