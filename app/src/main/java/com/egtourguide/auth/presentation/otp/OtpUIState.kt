package com.egtourguide.auth.presentation.otp

data class OtpUIState(
    val code: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: Boolean = false
)