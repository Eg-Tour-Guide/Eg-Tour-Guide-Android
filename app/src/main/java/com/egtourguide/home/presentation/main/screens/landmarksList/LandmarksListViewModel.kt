package com.egtourguide.home.presentation.main.screens.landmarksList

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.AbstractedLandmark
import com.egtourguide.core.domain.usecases.ChangeLandmarkSavedStateUseCase
import com.egtourguide.home.domain.usecases.DetectArtifactUseCase
import com.egtourguide.home.domain.usecases.GetLandmarksListUseCase
import com.egtourguide.home.presentation.filter.FilterScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandmarksListViewModel @Inject constructor(
    private val getLandmarksListUseCase: GetLandmarksListUseCase,
    private val changeLandmarkSavedStateUseCase: ChangeLandmarkSavedStateUseCase,
    private val detectArtifactUseCase: DetectArtifactUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LandmarksListUIState())
    val uiState = _uiState.asStateFlow()

    fun getLandmarksList(setLandmarkFilters: (tourismTypes: List<String>, locations: List<String>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            getLandmarksListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, callIsSent = true, error = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            landmarks = response.landmarks,
                            displayedLandmarks = response.landmarks
                        )
                    }

                    setLandmarkFilters(response.tourismTypes, response.locations)
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

    fun refreshLandmarks(setLandmarkFilters: (tourismTypes: List<String>, locations: List<String>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            getLandmarksListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isRefreshing = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            landmarks = response.landmarks
                        )
                    }
                    setLandmarkFilters(response.tourismTypes, response.locations)
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

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(isSaveError = false) }
    }

    fun filterLandmarks(filterState: FilterScreenState) {
        var landmarks = uiState.value.landmarks

        landmarks = landmarks.filter { landmark ->
            (landmark.category in filterState.appliedTourismTypes || filterState.appliedTourismTypes.isEmpty()) &&
                    (landmark.location in filterState.appliedLocations || filterState.appliedLocations.isEmpty()) &&
                    (landmark.rating >= filterState.appliedRating)
        }

        when (filterState.appliedSortBy) {
            1 -> {
                landmarks = landmarks.sortedByDescending { it.rating }
            }

            2 -> {
                landmarks = landmarks.sortedBy { it.rating }
            }
        }

        _uiState.update { it.copy(displayedLandmarks = landmarks) }
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

    fun onSaveClicked(place: AbstractedLandmark) {
        viewModelScope.launch(Dispatchers.IO) {
            place.isSaved = !place.isSaved
            _uiState.update { it.copy(isSaveCall = place.isSaved) }

            changeLandmarkSavedStateUseCase(placeId = place.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(isSaveError = true) }
                    place.isSaved = !place.isSaved
                },
                onNetworkError = {
                    _uiState.update { it.copy(isSaveError = true) }
                    place.isSaved = !place.isSaved
                }
            )
        }
    }
}