package com.egtourguide.customTours.presentation.customToursPlan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.customTours.domain.usecases.RemovePlaceFromTourUseCase
import com.egtourguide.expanded.domain.model.TourDetailsPlace
import com.egtourguide.expanded.domain.usecases.GetTourDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CustomToursPlanViewModel @Inject constructor(
    private val getTourDetailsUseCase: GetTourDetailsUseCase,
    private val removePlaceFromTourUseCase: RemovePlaceFromTourUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(CustomToursPlanScreenState())
    val uiState = _uiState.asStateFlow()

    fun getTourDetails(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getTourDetailsUseCase(tourId = id).onResponse(
                onLoading = {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            errorMessage = null,
                            callIsSent = true
                        )
                    }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            id = id,
                            isLoading = false,
                            title = response.name,
                            days = response.days,
                            chosenDay = response.days.keys.first()
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

    fun refreshTourDetails(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getTourDetailsUseCase(tourId = id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isRefreshing = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            id = id,
                            isRefreshing = false,
                            title = response.name,
                            days = response.days,
                            chosenDay = response.days.keys.first()
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

    fun changeChosenDay(day: Int) {
        _uiState.update { it.copy(chosenDay = day) }
    }

    fun removePlace(place: TourDetailsPlace, chosenDay: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            removePlaceFromTourUseCase(tourId = uiState.value.id, placeId = place.id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(showLoadingDialog = true) }
                },
                onSuccess = {
                    _uiState.update { it.copy(showLoadingDialog = false, isRemoveSuccess = true) }
                    handleDays(chosenDay = chosenDay, place = place)
                },
                onFailure = {
                    _uiState.update { it.copy(showLoadingDialog = false, isRemoveError = true) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(showLoadingDialog = false, isRemoveError = true) }
                }
            )
        }
    }

    private fun handleDays(
        chosenDay: Int,
        place: TourDetailsPlace
    ) {
        val newDays = uiState.value.days.toMutableMap()

        val updatedPlaces = newDays[chosenDay].orEmpty() - place
        newDays[chosenDay] = updatedPlaces

        if (updatedPlaces.isEmpty()) {
            newDays.remove(chosenDay)

            val leftNeighbor = newDays.keys.filter { it < chosenDay }.maxOrNull()
            val rightNeighbor = newDays.keys.filter { it > chosenDay }.minOrNull()

            val newChosenDay = leftNeighbor ?: rightNeighbor

            newChosenDay?.let { day ->
                _uiState.update { it.copy(chosenDay = day) }
            }
        }

        _uiState.update { it.copy(days = newDays) }
    }

    fun clearRemoveSuccess() {
        _uiState.update { it.copy(isRemoveSuccess = false) }
    }

    fun clearRemoveError() {
        _uiState.update { it.copy(isRemoveError = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}