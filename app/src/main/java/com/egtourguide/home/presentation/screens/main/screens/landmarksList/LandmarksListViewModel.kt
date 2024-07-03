package com.egtourguide.home.presentation.screens.main.screens.landmarksList

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.AbstractedLandmark
import com.egtourguide.core.domain.usecases.ChangeLandmarkSavedStateUseCase
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
    private val changeLandmarkSavedStateUseCase: ChangeLandmarkSavedStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(LandmarksListUIState())
    val uiState = _uiState.asStateFlow()
    var filters: HashMap<*, *>? = null

    fun onSaveClicked(place: AbstractedLandmark) {
        viewModelScope.launch(Dispatchers.IO) {
            place.isSaved = !place.isSaved

            changeLandmarkSavedStateUseCase(placeId = place.id).onResponse(
                onLoading = {},
                onSuccess = {
                    _uiState.update { it.copy(isSaveSuccess = true, isSave = place.isSaved) }
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
                    _uiState.update { it.copy(isLoading = true, callIsSent = true, error = null) }
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
        landMarks: List<AbstractedLandmark>,
        filters: HashMap<*, *>?
    ): List<AbstractedLandmark> {
        Log.d("`````TAG`````", "filterLandmarks: $filters")
        var resultedList = landMarks
        filters?.forEach { (filterKey, filterValue) ->
            filterKey as String
            filterValue as List<String>
            when (filterKey) {
                "Tourism Type" -> {
                    if (filterValue.isNotEmpty()) {
                        resultedList = resultedList.filter { item ->
                            filterValue.contains(item.category)
                        }
                    }
                }

                "Location" -> {
                    if (filterValue.isNotEmpty()) {
                        resultedList = resultedList.filter { item ->
                            filterValue.contains(item.location)
                        }
                    }
                }

                "Rating" -> {
                    if (filterValue.isNotEmpty()) {
                        val ratingValue = filterValue.first().toInt()
                        resultedList = resultedList.filter { item ->
                            item.rating >= ratingValue
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
        Log.d("````TAG````", "results: $resultedList")
        return resultedList
    }
}