package com.egtourguide.customTours.presentation.createTour

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.domain.validation.Validation
import com.egtourguide.core.domain.validation.ValidationCases
import com.egtourguide.core.utils.onResponse
import com.egtourguide.customTours.domain.usecases.CreateTourUseCase
import com.egtourguide.customTours.domain.usecases.EditTourUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreateTourViewModel @Inject constructor(
    private val createTourUseCase: CreateTourUseCase,
    private val editTourUseCase: EditTourUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CreateTourState())
    val uiState = _uiState.asStateFlow()

    fun setData(tourId: String, name: String, description: String) {
        _uiState.update {
            it.copy(
                tourId = tourId,
                name = name,
                description = description
            )
        }
    }

    fun onNameChanged(name: String) {
        _uiState.update { it.copy(name = name) }
    }

    fun onDescriptionChanged(description: String) {
        _uiState.update { it.copy(description = description) }
    }

    fun onCreateClicked() {
        _uiState.update {
            it.copy(
                nameError = ValidationCases.CORRECT,
                descriptionError = ValidationCases.CORRECT
            )
        }

        if (checkData()) {
            createTour()
        }
    }

    fun onSaveClicked() {
        _uiState.update {
            it.copy(
                nameError = ValidationCases.CORRECT,
                descriptionError = ValidationCases.CORRECT
            )
        }

        if (checkData()) {
            editTour()
        }
    }

    private fun checkData(): Boolean {
        val nameErrorState = Validation.validateName(_uiState.value.name)
        val descriptionErrorState = Validation.validateDescription(_uiState.value.description)

        _uiState.update {
            it.copy(
                nameError = nameErrorState,
                descriptionError = descriptionErrorState
            )
        }

        return nameErrorState == ValidationCases.CORRECT &&
                descriptionErrorState == ValidationCases.CORRECT
    }

    private fun createTour() {
        viewModelScope.launch(Dispatchers.IO) {
            createTourUseCase(
                name = _uiState.value.name,
                description = _uiState.value.description
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isCreateSuccess = true) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                }
            )
        }
    }

    private fun editTour() {
        viewModelScope.launch(Dispatchers.IO) {
            editTourUseCase(
                tourId = _uiState.value.tourId,
                name = _uiState.value.name,
                description = _uiState.value.description
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, isEditSuccess = true) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                }
            )
        }
    }
}