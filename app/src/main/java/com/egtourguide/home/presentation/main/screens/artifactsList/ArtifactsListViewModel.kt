package com.egtourguide.home.presentation.main.screens.artifactsList

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.core.domain.usecases.ChangeArtifactSavedStateUseCase
import com.egtourguide.home.domain.usecases.DetectArtifactUseCase
import com.egtourguide.home.domain.usecases.GetArtifactsListUseCase
import com.egtourguide.home.presentation.filter.FilterScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArtifactsListViewModel @Inject constructor(
    private val getArtifactsListUseCase: GetArtifactsListUseCase,
    private val changeArtifactSavedStateUseCase: ChangeArtifactSavedStateUseCase,
    private val detectArtifactUseCase: DetectArtifactUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ArtifactsListUIState())
    val uiState = _uiState.asStateFlow()

    fun getArtifactsList(setArtifactFilters: (artifactTypes: List<String>, materials: List<String>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            getArtifactsListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, callIsSent = true, error = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            artifacts = response.artifacts,
                            displayedArtifacts = response.artifacts
                        )
                    }

                    setArtifactFilters(response.artifactTypes, response.materials)
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

    fun refreshArtifacts(setArtifactFilters: (artifactTypes: List<String>, materials: List<String>) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            getArtifactsListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isRefreshing = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            artifacts = response.artifacts
                        )
                    }
                    setArtifactFilters(response.artifactTypes, response.materials)
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

    fun filterArtifacts(filterState: FilterScreenState) {
        var artifacts = uiState.value.artifacts

        artifacts = artifacts.filter { artifact ->
            (artifact.type in filterState.selectedArtifactTypes || filterState.selectedArtifactTypes.isEmpty()) &&
                    (artifact.material in filterState.selectedMaterials || filterState.selectedMaterials.isEmpty())
        }

        _uiState.update { it.copy(displayedArtifacts = artifacts) }
    }

    fun onSaveClicked(artifact: AbstractedArtifact) {
        viewModelScope.launch(Dispatchers.IO) {
            artifact.isSaved = !artifact.isSaved
            _uiState.update { it.copy(isSaveCall = artifact.isSaved) }

            changeArtifactSavedStateUseCase(artifactId = artifact.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(isSaveError = true) }
                    artifact.isSaved = !artifact.isSaved
                },
                onNetworkError = {
                    _uiState.update { it.copy(isSaveError = true) }
                    artifact.isSaved = !artifact.isSaved
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