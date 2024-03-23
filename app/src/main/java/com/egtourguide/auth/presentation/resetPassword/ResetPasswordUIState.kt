package com.egtourguide.auth.presentation.resetPassword

data class ResetPasswordUIState(
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isPasswordResetSuccess: Boolean = false
)
