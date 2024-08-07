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

    fun onSaveClicked(artifact: AbstractedArtifact) {
        viewModelScope.launch(Dispatchers.IO) {
            artifact.isSaved = !artifact.isSaved
            changeArtifactSavedStateUseCase(artifactId = artifact.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true, isSave = artifact.isSaved) }
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

    fun getArtifactsList() {
        viewModelScope.launch(Dispatchers.IO) {
            getArtifactsListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, callIsSent = true, error = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            artifacts = response,
                            displayedArtifacts = response
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

    // TODO: Add rest of filters!!
    fun filterArtifacts(filterState: FilterScreenState) {
        _uiState.update {
            it.copy(
                displayedArtifacts = it.artifacts.filter { artifact ->
                    artifact.material in filterState.selectedMaterials || filterState.selectedMaterials.isEmpty()
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