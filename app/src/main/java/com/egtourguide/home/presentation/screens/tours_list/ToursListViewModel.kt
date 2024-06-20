package com.egtourguide.home.presentation.screens.tours_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.domain.usecases.ChangeTourSavedStateUseCase
import com.egtourguide.home.domain.usecases.GetToursListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToursListViewModel @Inject constructor(
    private val getToursListUseCase: GetToursListUseCase,
    private val changeTourSavedStateUseCase: ChangeTourSavedStateUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ToursListUIState())
    val uiState = _uiState.asStateFlow()

    fun getToursList() {
        viewModelScope.launch(Dispatchers.IO) {
            getToursListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                },
                onSuccess = { response ->
                    _uiState.update { it.copy(isLoading = false, tours = response) }
                }
            )
        }
    }

    fun onSaveClicked(tour: AbstractedTour) {
        viewModelScope.launch(Dispatchers.IO) {
            changeTourSavedStateUseCase(tourId = tour.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true, isSave = !tour.isSaved) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(saveError = error) }
                }
            )
        }
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(saveError = null) }
    }
}