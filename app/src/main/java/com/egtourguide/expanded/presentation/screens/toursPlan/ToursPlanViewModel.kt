package com.egtourguide.expanded.presentation.screens.toursPlan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.expanded.domain.usecases.GetTourDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ToursPlanViewModel @Inject constructor(
    private val getTourDetailsUseCase: GetTourDetailsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(ToursPlanScreenState())
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
}