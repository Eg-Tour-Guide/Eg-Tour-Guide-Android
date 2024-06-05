package com.egtourguide.auth.presentation.resetPassword

import com.egtourguide.auth.domain.validation.ValidationCases

data class ResetPasswordUIState(
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val passwordError: ValidationCases = ValidationCases.CORRECT,
    val confirmPasswordError: ValidationCases = ValidationCases.CORRECT,
    val error: String? = null,
    val isPasswordResetSuccess: Boolean = false
)
