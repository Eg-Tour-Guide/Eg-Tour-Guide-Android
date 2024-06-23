package com.egtourguide.home.presentation.screens.toursPlan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.usecases.GetTourDetailsUseCase
import com.egtourguide.home.domain.usecases.UpdateTourDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToursPlanViewModel @Inject constructor(
    private val getTourDetailsUseCase: GetTourDetailsUseCase,
    private val updateTourDetailsUseCase: UpdateTourDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ToursPlanScreenState())
    val uiState = _uiState.asStateFlow()

    fun getTourDetails(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            getTourDetailsUseCase(tourId = id).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            title = "Test Title", // TODO: Change this!!,
                            days = response.days,
                            startDate = response.startDate,
                            chosenDate = 1647,
                            callIsSent = false,
                            showDatePicker = false
                        )
                    }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                }
            )
        }
    }

    fun changeTourDate() {
        viewModelScope.launch(Dispatchers.IO) {
            updateTourDetailsUseCase(
                tourId = _uiState.value.id,
                startDate = _uiState.value.chosenDate.toString() // TODO: Change this!!
            ).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, errorMessage = null) }
                },
                onSuccess = {
                    _uiState.update { it.copy(isLoading = false, startDate = it.chosenDate) }
                },
                onFailure = { message ->
                    _uiState.update { it.copy(isLoading = false, errorMessage = message) }
                }
            )
        }
    }

    fun changeDialogVisibility() {
        _uiState.update { it.copy(showDatePicker = !it.showDatePicker) }
    }

    fun changeChosenDay(day: Int) {
        _uiState.update { it.copy(chosenDay = day) }
    }

    fun onDateChanged(date: Long) {
        _uiState.update { it.copy(chosenDate = date) }
    }
}