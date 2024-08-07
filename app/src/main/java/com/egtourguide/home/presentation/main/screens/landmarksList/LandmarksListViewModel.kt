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

    fun onSaveClicked(place: AbstractedLandmark) {
        viewModelScope.launch(Dispatchers.IO) {
            place.isSaved = !place.isSaved

            changeLandmarkSavedStateUseCase(placeId = place.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true, isSave = place.isSaved) }
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

    fun getLandmarksList() {
        viewModelScope.launch(Dispatchers.IO) {
            getLandmarksListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, callIsSent = true, error = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            landmarks = response,
                            displayedLandmarks = response
                        )
                    }
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

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(saveError = null) }
    }

    // TODO: Add rest of filters!!
    fun filterLandmarks(filterState: FilterScreenState) {
        _uiState.update {
            it.copy(
                displayedLandmarks = it.landmarks.filter { landmark ->
                    landmark.location in filterState.selectedLocations || filterState.selectedLocations.isEmpty()
                }
            )
        }
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
                            detectedArtifact = response,
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isDetectionLoading = false, error = error) }
                },
                onNetworkError = {
                    // TODO: Show detection error!!
                    _uiState.update { it.copy(isDetectionLoading = false) }
                }
            )
        }
    }

    fun clearDetectionSuccess() {
        _uiState.update { it.copy(detectedArtifact = null) }
    }
}