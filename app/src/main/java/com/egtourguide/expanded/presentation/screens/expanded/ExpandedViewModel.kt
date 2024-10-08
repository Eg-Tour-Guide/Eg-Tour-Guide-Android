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
import com.egtourguide.core.utils.ExpandedType.*
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
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            callIsSent = true,
                            errorMessage = null
                        )
                    }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
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
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    private fun getLandmark(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getLandmarkUseCase(id = id).onResponse(
                onLoading = {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            callIsSent = true,
                            errorMessage = null
                        )
                    }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            id = response.id,
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
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    private fun getArtifact(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getArtifactUseCase(id = id).onResponse(
                onLoading = {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            callIsSent = true,
                            errorMessage = null
                        )
                    }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            id = response.id,
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
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    private fun getTour(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getTourUseCase(tourId = id).onResponse(
                onLoading = {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            callIsSent = true,
                            errorMessage = null,
                        )
                    }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
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
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    fun refreshData(id: String, expandedType: String) {
        when (expandedType) {
            EVENT.name -> refreshEvent(id = id)
            LANDMARK.name -> refreshLandmark(id = id)
            ARTIFACT.name -> refreshArtifact(id = id)
            else -> refreshTour(id = id)
        }
    }

    private fun refreshEvent(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getEventUseCase(eventId = id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isRefreshing = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
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
                onFailure = {
                    _uiState.update { it.copy(isRefreshing = false) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
            )
        }
    }

    private fun refreshLandmark(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getLandmarkUseCase(id = id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isRefreshing = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            id = response.id,
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
                onFailure = {
                    _uiState.update { it.copy(isRefreshing = false) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
            )
        }
    }

    private fun refreshArtifact(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getArtifactUseCase(id = id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isRefreshing = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
                            id = response.id,
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
                onFailure = {
                    _uiState.update { it.copy(isRefreshing = false) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
            )
        }
    }

    private fun refreshTour(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getTourUseCase(tourId = id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isRefreshing = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isRefreshing = false,
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
                onFailure = {
                    _uiState.update { it.copy(isRefreshing = false) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isRefreshing = false) }
                }
            )
        }
    }

    fun changeSavedState(expandedType: String) {
        val successLogic = {
            _uiState.update { it.copy(isSaveSuccess = true) }
        }

        val errorLogic = {
            _uiState.update { it.copy(isSaveError = true, isSaved = !it.isSaved) }
        }

        viewModelScope.launch(Dispatchers.IO) {
            _uiState.update { it.copy(isSaved = !it.isSaved, isSaveCall = !it.isSaved) }

            when (expandedType) {
                LANDMARK.name -> {
                    changeLandmarkSavedStateUseCase(placeId = _uiState.value.id).onResponse(
                        onLoading = {},
                        onSuccess = { successLogic() },
                        onFailure = { errorLogic() },
                        onNetworkError = errorLogic
                    )
                }

                ARTIFACT.name -> {
                    changeArtifactSavedStateUseCase(artifactId = _uiState.value.id).onResponse(
                        onLoading = {},
                        onSuccess = { successLogic() },
                        onFailure = { errorLogic() },
                        onNetworkError = errorLogic
                    )
                }

                else -> {
                    changeTourSavedStateUseCase(tourId = _uiState.value.id).onResponse(
                        onLoading = {},
                        onSuccess = { successLogic() },
                        onFailure = { errorLogic() },
                        onNetworkError = errorLogic
                    )
                }
            }
        }
    }

    fun changePlaceSavedState(place: AbstractedLandmark) {
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

    fun changeArtifactSavedState(artifact: AbstractedArtifact) {
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

    fun changeTourSavedState(tour: AbstractedTour) {
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

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(isSaveError = false) }
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

    fun addToTourClicked(duration: Int) {
        if (!checkData(duration)) {
            changeAddDialogVisibility()
            addToTour(duration = duration)
        }
    }

    private fun checkData(duration: Int): Boolean {
        _uiState.update {
            it.copy(
                isDurationError = duration <= 0,
                isTourError = it.tourID.isEmpty()
            )
        }

        return _uiState.value.isDurationError || _uiState.value.isTourError
    }

    private fun addToTour(duration: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            addPlaceToTourUseCase(
                tourId = _uiState.value.tourID,
                placeId = _uiState.value.id,
                time = duration
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(showLoadingDialog = true) }
                },
                onSuccess = {
                    _uiState.update { it.copy(showLoadingDialog = false, showAddSuccess = true) }
                },
                onFailure = {
                    _uiState.update { it.copy(showLoadingDialog = false, showAddError = true) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(showLoadingDialog = false, isNetworkError = true) }
                }
            )
        }
    }

    fun clearAddSuccess() {
        _uiState.update { it.copy(showAddSuccess = false) }
    }

    fun clearAddError() {
        _uiState.update { it.copy(showAddError = false) }
    }
}