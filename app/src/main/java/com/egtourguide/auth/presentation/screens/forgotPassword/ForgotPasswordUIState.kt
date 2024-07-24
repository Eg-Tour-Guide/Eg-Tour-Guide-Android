package com.egtourguide.auth.presentation.screens.forgotPassword

import com.egtourguide.core.domain.validation.ValidationCases

data class ForgotPasswordUIState(
    val email: String = "",
    val emailError: ValidationCases = ValidationCases.CORRECT,
    val isLoading: Boolean = false,
    val error: String? = null,
    val successMessage: String? = null,
    val isNetworkError: Boolean = false,
    val code: String = ""
)
