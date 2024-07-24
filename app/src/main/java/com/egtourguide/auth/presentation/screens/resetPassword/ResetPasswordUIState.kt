package com.egtourguide.auth.presentation.screens.resetPassword

import com.egtourguide.core.domain.validation.ValidationCases

data class ResetPasswordUIState(
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val passwordError: ValidationCases = ValidationCases.CORRECT,
    val confirmPasswordError: ValidationCases = ValidationCases.CORRECT,
    val isError: Boolean = false,
    val isPasswordResetSuccess: Boolean = false,
    val isNetworkError: Boolean = false
)
