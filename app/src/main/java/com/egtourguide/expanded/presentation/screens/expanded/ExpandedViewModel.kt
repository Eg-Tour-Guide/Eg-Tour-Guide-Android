package com.egtourguide.expanded.presentation.screens.expanded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.domain.model.AbstractedLandmark
import com.egtourguide.expanded.domain.usecases.AddPlaceToTourUseCase
import com.egtourguide.core.domain.usecases.ChangeArtifactSavedStateUseCase
import com.egtourguide.core.domain.usecases.ChangeLandmarkSavedStateUseCase
import com.egtourguide.core.domain.usecases.ChangeTourSavedStateUseCase
import com.egtourguide.expanded.domain.usecases.GetArtifactUseCase
import com.egtourguide.expanded.domain.usecases.GetEventUseCase
import com.egtourguide.expanded.domain.usecases.GetLandmarkUseCase
import com.egtourguide.expanded.domain.usecases.GetTourUseCase
import com.egtourguide.expanded.presentation.screens.expanded.ExpandedType.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExpandedViewModel @Inject constructor(
    private val getEventUseCase: GetEventUseCase,
    private val getLandmarkUseCase: GetLandmarkUseCase,
    private val getArtifactUseCase: GetArtifactUseCase,
    private val getTourUseCase: GetTourUseCase,
    private val changeLandmarkSavedStateUseCase: ChangeLandmarkSavedStateUseCase,
    private val changeArtifactSavedStateUseCase: ChangeArtifactSavedStateUseCase,
    private val changeTourSavedStateUseCase: ChangeTourSavedStateUseCase,
    private val addPlaceToTourUseCase: AddPlaceToTourUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ExpandedScreenState())
    val uiState = _uiState.asStateFlow()

    fun getData(id: String, expandedType: String) {
        when (expandedType) {
            EVENT.name -> getEvent(id = id)
            LANDMARK.name -> getLandmark(id = id)
            ARTIFACT.name -> getArtifact(id = id)
            else -> getTour(id = id)
        }
    }

    private fun getEvent(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getEventUseCase(eventId = id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            callIsSent = true,
                            id = response.id,
                            images = response.images,
                            title = response.name,
                            date = response.date,
                            latitute = response.latitude,
                            longitude = response.longitude,
                            tourismTypes = response.category,
                            description = response.description,
                            location = response.placeName
                        )
                    }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                }
            )
        }
    }

    private fun getLandmark(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getLandmarkUseCase(id = id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, id = id) }
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
                    _uiState.update { it.copy(isLoading = true, id = id) }
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

    private fun getTour(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getTourUseCase(tourId = id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            callIsSent = true,
                            id = response.id,
                            images = response.images,
                            title = response.name,
                            reviewsAverage = response.ratingAverage,
                            reviewsCount = response.ratingQuantity,
                            reviews = response.reviews,
                            tourismTypes = response.type,
                            duration = response.duration,
                            isSaved = response.saved,
                            description = response.description,
                            relatedTours = response.relatedTours
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

            changeLandmarkSavedStateUseCase(placeId = _uiState.value.id).onResponse(
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

    fun changePlaceSavedState(place: AbstractedLandmark) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSaveCall = !place.isSaved) }
            place.isSaved = !place.isSaved

            changeLandmarkSavedStateUseCase(placeId = place.id).onResponse(
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

    fun changeTourSavedState(tour: AbstractedTour) {
        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSaveCall = !tour.isSaved) }
            tour.isSaved = !tour.isSaved

            changeTourSavedStateUseCase(tourId = tour.id).onResponse(
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
            addPlaceToTourUseCase(
                tourId = _uiState.value.tourID,
                placeId = _uiState.value.id,
                time = duration
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(showLoadingDialog = true, errorMessage = null) }
                },
                onSuccess = {
                    _uiState.update { it.copy(showLoadingDialog = false, showAddSuccess = true) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(showLoadingDialog = false, errorMessage = message) }
                }
            )
        }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(showAddSuccess = false) }
    }
}