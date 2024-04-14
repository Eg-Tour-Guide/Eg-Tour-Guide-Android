package com.egtourguide.auth.presentation.otp

import com.egtourguide.auth.domain.validation.ValidationCases

data class OtpUIState(
    val code: String = "",
    val isLoading: Boolean = false,
    val isVerifiedSuccessfully: Boolean = false,
    val isSignedSuccessfully: Boolean = false,
    val errorMessage: String? = null,
    val codeError: ValidationCases = ValidationCases.CORRECT
)