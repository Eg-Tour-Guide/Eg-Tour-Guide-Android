package com.egtourguide.auth.presentation.forgotPassword

data class ForgotPasswordUIState(
    val email: String = "",
    val isLoading: Boolean = false,
    val error: String? = null,
    val isCodeSentSuccessfully: Boolean = false,
    val successMessage: String? = null,
    val code: String = ""
)
