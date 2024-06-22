package com.egtourguide.home.presentation.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.domain.usecases.ChangePlaceSavedStateUseCase
import com.egtourguide.home.domain.usecases.GetHomeUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeUseCase: GetHomeUseCase,
    private val changePlaceSavedStateUseCase: ChangePlaceSavedStateUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUIState())
    val uiState = _uiState.asStateFlow()

    fun getHome() {
        viewModelScope.launch(Dispatchers.IO) {
            getHomeUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            events = response.event,
                            suggestedPlaces = response.suggestedForYou,
                            topRatedPlaces = response.topRated,
                            explorePlaces = response.explore,
                            recentlyAddedPlaces = response.recentlyAdded,
                            mightLikePlaces = response.suggestedForYou,
                            recentlyViewedPlaces = response.recentlyViewed
                        )
                    }
                }
            )
        }
    }

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

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(saveError = null) }
    }

}