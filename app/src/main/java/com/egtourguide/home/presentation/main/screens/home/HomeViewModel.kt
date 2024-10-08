package com.egtourguide.home.presentation.main.screens.home

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.AbstractedLandmark
import com.egtourguide.core.domain.usecases.ChangeLandmarkSavedStateUseCase
import com.egtourguide.home.domain.usecases.DetectArtifactUseCase
import com.egtourguide.home.domain.usecases.GetHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeUseCase: GetHomeUseCase,
    private val changeLandmarkSavedStateUseCase: ChangeLandmarkSavedStateUseCase,
    private val detectArtifactUseCase: DetectArtifactUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState = _uiState.asStateFlow()

    fun getHome() {
        viewModelScope.launch(Dispatchers.IO) {
            getHomeUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, callIsSent = true, error = null) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            events = response.event,
                            suggestedPlaces = response.suggestedForYou,
                            topRatedPlaces = response.topRated,
                            explorePlaces = response.explore,
                            recentlyAddedPlaces = response.recentlyAdded,
                            recentlyViewedPlaces = response.recentlyViewed
                        )
                    }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isNetworkError = true, isLoading = false) }
                }
            )
        }
    }

    fun refreshHome() {
        viewModelScope.launch(Dispatchers.IO) {
            getHomeUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isRefreshing = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            events = response.event,
                            suggestedPlaces = response.suggestedForYou,
                            topRatedPlaces = response.topRated,
                            explorePlaces = response.explore,
                            recentlyAddedPlaces = response.recentlyAdded,
                            recentlyViewedPlaces = response.recentlyViewed
                        )
                    }
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

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(isSaveError = false) }
    }

    fun clearDetectionError() {
        _uiState.update { it.copy(isDetectionError = false) }
    }

    fun clearDetectionSuccess() {
        _uiState.update { it.copy(detectedArtifact = null) }
    }
}