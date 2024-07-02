package com.egtourguide.home.presentation.screens.main.screens.tours_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.domain.usecases.ChangeTourSavedStateUseCase
import com.egtourguide.home.domain.usecases.GetToursListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.HashMap
import javax.inject.Inject

@HiltViewModel
class ToursListViewModel @Inject constructor(
    private val getToursListUseCase: GetToursListUseCase,
    private val changeTourSavedStateUseCase: ChangeTourSavedStateUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ToursListUIState())
    val uiState = _uiState.asStateFlow()
    var filters: HashMap<*, *>? = null

    fun getToursList() {
        viewModelScope.launch(Dispatchers.IO) {
            getToursListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, error = null) }
                },
                onSuccess = { response ->
                    val tours = filterTours(tours = response, filters = filters)
                    _uiState.update {
                        it.copy(
                            callIsSent = true,
                            isLoading = false,
                            tours = tours
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                }
            )
        }
    }

    fun onSaveClicked(tour: AbstractedTour) {
        viewModelScope.launch(Dispatchers.IO) {
            changeTourSavedStateUseCase(tourId = tour.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true, isSave = !tour.isSaved) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(saveError = error) }
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

    private fun filterTours(
        tours: List<AbstractedTour>,
        filters: HashMap<*, *>?
    ): List<AbstractedTour> {
        var resultedList = tours
        filters?.forEach { (filterKey, filterValue) ->
            filterKey as String
            filterValue as List<String>
            when (filterKey) {
                /*"Tour Type" -> {
                    resultedList = resultedList.filter { item ->
                        filterValue.contains(item.type)
                    }
                }*/

                /*"Location" -> {
                    resultedList = resultedList.filter { item ->
                        filterValue.contains(item.location)
                    }
                }*/

                "Rating" -> {
                    if (filterValue.isNotEmpty()) {
                        val ratingValue = filterValue.first().toInt()
                        resultedList = resultedList.filter { item ->
                            item.rating >= ratingValue
                        }
                    }
                }

                "Duration" -> {
                    if (filterValue.size == 2) {
                        val minDays = filterValue.first().toInt()
                        val maxDays = filterValue.last().toInt()
                        resultedList = resultedList.filter { item ->
                            item.duration in minDays..maxDays
                        }
                    }
                }

                "Sort By" -> {
                    if (filterValue.isNotEmpty()) {
                        val sortType = filterValue.first().toInt()
                        resultedList = if (sortType == 0) {
                            resultedList.sortedBy { item -> item.rating }
                        } else {
                            resultedList.sortedByDescending { item -> item.rating }
                        }
                    }
                }
            }
        }
        return resultedList
    }
}