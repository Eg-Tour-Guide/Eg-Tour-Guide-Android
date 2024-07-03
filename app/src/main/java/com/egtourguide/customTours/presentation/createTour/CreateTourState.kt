package com.egtourguide.customTours.presentation.createTour

import com.egtourguide.core.domain.validation.ValidationCases

data class CreateTourState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isCreateSuccess: Boolean = false,
    val isEditSuccess: Boolean = false,
    val tourId: String = "",
    val name: String = "",
    val description: String = "",
    val nameError: ValidationCases = ValidationCases.CORRECT,
    val descriptionError: ValidationCases = ValidationCases.CORRECT
)
