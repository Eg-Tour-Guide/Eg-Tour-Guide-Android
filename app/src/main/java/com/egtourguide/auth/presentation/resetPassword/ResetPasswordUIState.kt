package com.egtourguide.auth.presentation.resetPassword

data class ResetPasswordUIState(
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val passwordError: String? = null,
    val confirmPasswordError: String? = null,
    val isPasswordResetSuccess: Boolean = false
)
