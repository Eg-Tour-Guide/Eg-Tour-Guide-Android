package com.egtourguide.home.presentation.screens.landmarks_list

import android.media.Rating
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.domain.usecases.ChangePlaceSavedStateUseCase
import com.egtourguide.home.domain.usecases.GetLandmarksListUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LandmarksListViewModel @Inject constructor(
    private val getLandmarksListUseCase: GetLandmarksListUseCase,
    private val changePlaceSavedStateUseCase: ChangePlaceSavedStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LandmarksListUIState())
    val uiState = _uiState.asStateFlow()
    var filters: HashMap<*, *>? = null

    fun onSaveClicked(place: Place) {
        viewModelScope.launch(Dispatchers.IO) {
            changePlaceSavedStateUseCase(placeId = place.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true, isSave = !place.isSaved) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(saveError = error) }
                }
            )
        }
    }

    fun getLandmarksList() {
        viewModelScope.launch(Dispatchers.IO) {
            getLandmarksListUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                },
                onSuccess = { response ->
                    val landmarks = filterLandmarks(landMarks = response, filters = filters)
                    _uiState.update { it.copy(isLoading = false, landmarks = landmarks) }
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

    private fun filterLandmarks(
        landMarks: List<Place>,
        filters: HashMap<*, *>?
    ): List<Place> {
        var resultedList = landMarks
        filters?.forEach { (filterKey, filterValue) ->
            filterKey as String
            filterValue as List<String>
            when (filterKey) {
                "Tourism Type" -> {
                    resultedList = resultedList.filter { item ->
                        filterValue.contains(item.category)
                    }
                }

                "Location" -> {
                    resultedList = resultedList.filter { item ->
                        filterValue.contains(item.location)
                    }
                }

                "Rating" -> {
                    val ratingValue = filterValue.first().toInt()
                    resultedList = resultedList.filter { item ->
                        item.rating >= ratingValue
                    }
                }

                "Sort By" -> {
                    val sortType = filterValue.first().toInt()
                    resultedList = if (sortType == 0) {
                        resultedList.sortedBy { item -> item.rating }
                    } else {
                        resultedList.sortedByDescending { item -> item.rating }
                    }
                }
            }
        }
        return resultedList
    }
}