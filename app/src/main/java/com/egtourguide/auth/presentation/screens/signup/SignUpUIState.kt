package com.egtourguide.auth.presentation.screens.signup

import com.egtourguide.auth.domain.validation.ValidationCases

data class SignUpUIState(
    val name: String = "",
    val email: String = "",
    val phone: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isSuccess: Boolean = false,
    val code: String = "",
    val nameError: ValidationCases = ValidationCases.CORRECT,
    val emailError: ValidationCases = ValidationCases.CORRECT,
    val phoneError: ValidationCases = ValidationCases.CORRECT,
    val passwordError: ValidationCases = ValidationCases.CORRECT,
    val confirmPasswordError: ValidationCases = ValidationCases.CORRECT
)
