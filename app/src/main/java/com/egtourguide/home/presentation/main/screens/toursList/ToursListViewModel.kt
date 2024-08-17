package com.egtourguide.home.presentation.main.screens.toursList

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.core.domain.usecases.ChangeTourSavedStateUseCase
import com.egtourguide.home.domain.usecases.DetectArtifactUseCase
import com.egtourguide.home.domain.usecases.GetToursListUseCase
import com.egtourguide.home.presentation.filter.FilterScreenState
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
    private val detectArtifactUseCase: DetectArtifactUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ToursListUIState())
    val uiState = _uiState.asStateFlow()

    fun getToursList(setFilters: (List<String>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            getToursListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, callIsSent = true, error = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            tours = response.tours,
                            displayedTours = response.tours
                        )
                    }

                    setFilters(response.tourTypes)
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isNetworkError = true, isLoading = false) }
                }
            )
        }
    }

    fun refreshTours(setFilters: (List<String>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            getToursListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isRefreshing = true) }
                },
                onSuccess = { response ->
                    _uiState.update { it.copy(isRefreshing = false, tours = response.tours) }
                    setFilters(response.tourTypes)
                },
                onFailure = {
                    _uiState.update { it.copy(isRefreshing = false) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
            )
        }
    }

    fun onSaveClicked(tour: AbstractedTour) {
        viewModelScope.launch(Dispatchers.IO) {
            tour.isSaved = !tour.isSaved
            _uiState.update { it.copy(isSaveCall = tour.isSaved) }

            changeTourSavedStateUseCase(tourId = tour.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(isSaveError = true) }
                    tour.isSaved = !tour.isSaved
                },
                onNetworkError = {
                    _uiState.update { it.copy(isSaveError = true) }
                    tour.isSaved = !tour.isSaved
                }
            )
        }
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(isSaveError = false) }
    }

    fun filterTours(filterState: FilterScreenState) {
        var tours = uiState.value.tours

        tours = tours.filter { tour ->
            tour.duration >= filterState.appliedMinDuration.toInt() &&
                    tour.duration <= filterState.appliedMaxDuration.toInt() &&
                    tour.rating >= filterState.appliedRating
        }

        when (filterState.appliedSortBy) {
            1 -> {
                tours = tours.sortedByDescending { it.rating }
            }

            2 -> {
                tours = tours.sortedBy { it.rating }
            }
        }

        // TODO: Filter tour types!!
//        if(filterState.appliedTourTypes.isNotEmpty()) {
//            tours.filter { it.type in filterState.appliedTourTypes }
//        }

        _uiState.update { it.copy(displayedTours = tours) }
    }

    fun detectArtifact(image: Bitmap) {
        viewModelScope.launch(Dispatchers.IO) {
            detectArtifactUseCase(bitmap = image).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isDetectionLoading = true, detectedArtifact = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isDetectionLoading = false,
                            detectedArtifact = response
                        )
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isDetectionLoading = false, isDetectionError = true) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isDetectionLoading = false, isDetectionError = true) }
                }
            )
        }
    }

    fun clearDetectionSuccess() {
        _uiState.update { it.copy(detectedArtifact = null) }
    }

    fun clearDetectionError() {
        _uiState.update { it.copy(isDetectionError = false) }
    }
}