package com.egtourguide.home.presentation.screens.expanded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.domain.usecases.ChangeArtifactSavedStateUseCase
import com.egtourguide.home.domain.usecases.ChangePlaceSavedStateUseCase
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
    private val getArtifactUseCase: GetArtifactUseCase,
    private val changePlaceSavedStateUseCase: ChangePlaceSavedStateUseCase,
    private val changeArtifactSavedStateUseCase: ChangeArtifactSavedStateUseCase
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
                            isSaved = response.saved,
                            location = response.location,
                            reviewsAverage = response.reviewsAverage,
                            reviewsCount = response.reviewsCount,
                            reviews = response.reviews,
                            tourismTypes = response.type,
                            description = response.description,
                            includedArtifacts = response.includedArtifacts,
                            relatedPlaces = response.relatedPlaces,
                            latitute = response.latitude,
                            longitude = response.longitude,
                            vrModel = response.model
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
                            isSaved = response.saved,
                            location = response.museum,
                            description = response.description,
                            artifactType = response.type,
                            artifactMaterials = response.material,
                            relatedArtifacts = response.relatedArtifacts,
                            arModel = response.arModel
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
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSaved = !it.isSaved, isSaveCall = !it.isSaved) }

            changePlaceSavedStateUseCase(placeId = _uiState.value.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(errorMessage = message) }
                }
            )
        }
    }

    fun changePlaceSavedState(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSaveCall = !place.isSaved) }
            place.isSaved = !place.isSaved

            changePlaceSavedStateUseCase(placeId = place.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(errorMessage = message) }
                }
            )
        }
    }

    fun changeArtifactSavedState(artifact: AbstractedArtifact) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSaveCall = !artifact.isSaved) }
            artifact.isSaved = !artifact.isSaved

            changeArtifactSavedStateUseCase(artifactId = artifact.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(errorMessage = message) }
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

    fun changeAddDialogVisibility() {
        _uiState.update {
            it.copy(
                showAddDialog = !it.showAddDialog,
                tourID = "",
                tourName = "",
                tourImage = ""
            )
        }
    }

    fun changeTourData(id: String, name: String, image: String) {
        _uiState.update {
            it.copy(
                tourID = id,
                tourName = name,
                tourImage = image
            )
        }
    }

    fun addToTour(duration: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            // TODO: Implement this!!
        }
    }
}