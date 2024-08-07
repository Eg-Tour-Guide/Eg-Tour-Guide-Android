package com.egtourguide.customTours.presentation.myTours

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.core.domain.usecases.ChangeTourSavedStateUseCase
import com.egtourguide.customTours.domain.usecases.GetMyToursUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MyToursViewModel @Inject constructor(
    private val getMyToursUseCase: GetMyToursUseCase,
    private val changeTourSavedStateUseCase: ChangeTourSavedStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyToursUIState())
    val uiState = _uiState.asStateFlow()

    fun getMyTours() {
        viewModelScope.launch(Dispatchers.IO) {
            getMyToursUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, isCallSent = true) }
                },
                onSuccess = { response ->
                    _uiState.update { it.copy(isLoading = false, myTours = response) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
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
                },
                onNetworkError = {
                    // TODO: Show save error!!
                    _uiState.update { it.copy(isLoading = false) }
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

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}