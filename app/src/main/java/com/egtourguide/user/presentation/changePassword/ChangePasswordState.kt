package com.egtourguide.user.presentation.changePassword

import com.egtourguide.core.domain.validation.ValidationCases

data class ChangePasswordState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val oldPassword: String = "",
    val newPassword: String = "",
    val confirmPassword: String = "",
    val oldPasswordError: ValidationCases = ValidationCases.CORRECT,
    val newPasswordError: ValidationCases = ValidationCases.CORRECT,
    val confirmPasswordError: ValidationCases = ValidationCases.CORRECT
)
