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
                            days = response.days
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
        newDays[chosenDay] = newDays[chosenDay].orEmpty() - place
        if (newDays[chosenDay].isNullOrEmpty()) {
            // TODO: Check this logic!!
            if (chosenDay != newDays.keys.first()) _uiState.update { it.copy(chosenDay = chosenDay - 1) }
            else _uiState.update { it.copy(chosenDay = chosenDay + 1) }

            newDays.remove(chosenDay)
        }
        _uiState.update { it.copy(days = newDays) }
    }

    fun clearSuccess() {
        _uiState.update { it.copy(isRemoveSuccess = false) }
    }

    fun clearError() {
        _uiState.update { it.copy(isRemoveError = false) }
    }
}