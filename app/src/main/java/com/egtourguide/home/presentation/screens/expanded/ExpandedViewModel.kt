package com.egtourguide.home.presentation.screens.expanded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.usecases.GetArtifactUseCase
import com.egtourguide.home.domain.usecases.GetLandmarkUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpandedViewModel @Inject constructor(
    private val getLandmarkUseCase: GetLandmarkUseCase,
    private val getArtifactUseCase: GetArtifactUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpandedScreenState())
    val uiState = _uiState.asStateFlow()

    fun getData(id: String, isLandmark: Boolean) {
        if (isLandmark) getLandmark(id = id)
        else getArtifact(id = id)
    }

    private fun getLandmark(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getLandmarkUseCase(id = id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, id = id, isLandmark = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            callIsSent = true,
                            images = response.images,
                            title = response.title,
                            location = response.location,
                            reviewsAverage = 4.5,
                            reviews = response.reviews,
                            tourismTypes = response.type,
                            description = response.description,
                            includedArtifacts = emptyList(),
                            relatedPlaces = response.relatedPlaces,
                            latitute = response.latitude,
                            longitude = response.longitude
                        )
                    }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                }
            )
        }
    }

    private fun getArtifact(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getArtifactUseCase(id = id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, id = id, isLandmark = false) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            callIsSent = true,
                            images = response.images,
                            title = response.title,
                            reviewsAverage = 4.5,
                            reviews = emptyList(),
                            description = response.description,
                            artifactType = "",
                            artifactMaterials = emptyList(),
                            relatedArtifacts = response.relatedArtifacts
                        )
                    }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                }
            )
        }
    }

    fun changeSavedState() {
        // TODO: Implement this!!
        _uiState.update { it.copy(isSaved = !it.isSaved) }
    }
}