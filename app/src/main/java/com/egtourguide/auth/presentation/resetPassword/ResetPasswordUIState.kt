package com.egtourguide.auth.presentation.resetPassword

data class ResetPasswordUIState(
    val password: String = "",
    val confirmPassword: String = ""
)
