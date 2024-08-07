package com.egtourguide.customTours.presentation.customExpanded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.domain.usecases.ChangeTourSavedStateUseCase
import com.egtourguide.core.utils.onResponse
import com.egtourguide.expanded.domain.usecases.GetTourUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomExpandedViewModel @Inject constructor(
    private val getTourUseCase: GetTourUseCase,
    private val changeTourSavedStateUseCase: ChangeTourSavedStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomExpandedState())
    val uiState = _uiState.asStateFlow()

    fun getData(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getTourUseCase(tourId = id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            callIsSent = true,
                            id = response.id,
                            images = response.images,
                            title = response.name,
                            duration = response.duration,
                            isSaved = response.saved,
                            description = response.description
                        )
                    }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    fun onSaveClicked() {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSaved = !it.isSaved, isSaveCall = !it.isSaved) }

            changeTourSavedStateUseCase(tourId = _uiState.value.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(errorMessage = message) }
                },
                onNetworkError = {
                    // TODO: Show save error!!
                    _uiState.update { it.copy(isLoading = false) }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }
}