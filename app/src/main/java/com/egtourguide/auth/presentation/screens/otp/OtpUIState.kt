package com.egtourguide.auth.presentation.screens.otp

import com.egtourguide.core.domain.validation.ValidationCases

data class OtpUIState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val sentCode: String = "",
    val code: String = "",
    val isLoading: Boolean = false,
    val isVerifiedSuccessfully: Boolean = false,
    val isSignedSuccessfully: Boolean = false,
    val isNetworkError: Boolean = false,
    val errorMessage: String? = null,
    val codeError: ValidationCases = ValidationCases.CORRECT
)