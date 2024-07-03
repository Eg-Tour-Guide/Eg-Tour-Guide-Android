package com.egtourguide.auth.presentation.screens.login

import com.egtourguide.core.domain.validation.ValidationCases

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val emailError: ValidationCases = ValidationCases.CORRECT,
    val passwordError: ValidationCases = ValidationCases.CORRECT
)
