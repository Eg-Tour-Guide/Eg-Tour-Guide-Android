package com.egtourguide.auth.presentation.reset_password

data class ResetPasswordUIState(
    val password: String = "",
    val confirmPassword: String = ""
)
