package com.egtourguide.user.presentation.editProfile

import com.egtourguide.core.domain.validation.ValidationCases

data class EditProfileState(
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val name: String = "",
    val phone: String = "",
    val email: String = "",
    val nameError: ValidationCases = ValidationCases.CORRECT,
    val phoneError: ValidationCases = ValidationCases.CORRECT,
    val emailError: ValidationCases = ValidationCases.CORRECT
)