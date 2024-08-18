package com.egtourguide.user.presentation.saved

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.user.domain.model.AbstractSavedItem
import com.egtourguide.core.domain.usecases.ChangeArtifactSavedStateUseCase
import com.egtourguide.core.domain.usecases.ChangeLandmarkSavedStateUseCase
import com.egtourguide.core.domain.usecases.ChangeTourSavedStateUseCase
import com.egtourguide.core.utils.ItemType
import com.egtourguide.home.presentation.filter.FilterScreenState
import com.egtourguide.user.domain.usecases.GetSavedItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val getSavedItemsUseCase: GetSavedItemsUseCase,
    private val changeLandmarkSavedStateUseCase: ChangeLandmarkSavedStateUseCase,
    private val changeTourSavedStateUseCase: ChangeTourSavedStateUseCase,
    private val changeArtifactSavedStateUseCase: ChangeArtifactSavedStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SavedScreenUIState())
    val uiState = _uiState.asStateFlow()

    fun getSavedItems() {
        viewModelScope.launch(Dispatchers.IO) {
            getSavedItemsUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, error = null, isCallSent = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            savedList = response,
                            displayedSavedList = response
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    fun refreshSaved() {
        viewModelScope.launch(Dispatchers.IO) {
            getSavedItemsUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isRefreshing = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            savedList = response,
                            displayedSavedList = response,
                            isRefreshing = false,
                            refreshFilters = true
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

    fun onSaveClicked(item: AbstractSavedItem) {
        val successLogic = {
            _uiState.update { it.copy(isSaveSuccess = true) }
        }

        val errorLogic = {
            _uiState.update { it.copy(isSaveError = true) }
            item.isSaved = !item.isSaved
        }

        viewModelScope.launch(Dispatchers.IO) {
            item.isSaved = !item.isSaved
            _uiState.update { it.copy(isSaveCall = item.isSaved) }

            when (item.itemType) {
                ItemType.ARTIFACT -> {
                    changeArtifactSavedStateUseCase(artifactId = item.id).onResponse(
                        onLoading = {},
                        onSuccess = { successLogic() },
                        onFailure = { errorLogic() },
                        onNetworkError = errorLogic
                    )
                }

                ItemType.TOUR -> {
                    changeTourSavedStateUseCase(item.id).onResponse(
                        onLoading = {},
                        onSuccess = { successLogic() },
                        onFailure = { errorLogic() },
                        onNetworkError = errorLogic
                    )
                }

                ItemType.LANDMARK -> {
                    changeLandmarkSavedStateUseCase(item.id).onResponse(
                        onLoading = {},
                        onSuccess = { successLogic() },
                        onFailure = { errorLogic() },
                        onNetworkError = errorLogic
                    )
                }
            }
        }
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(isSaveError = false) }
    }

    fun whenFiltersRefreshed() {
        _uiState.update { it.copy(refreshFilters = false) }
    }

    fun filterSavedItems(filterState: FilterScreenState) {
        var savedList = uiState.value.savedList

        when (filterState.appliedCategory) {
            "Landmarks" -> {
                savedList = savedList.filter { it.itemType == ItemType.LANDMARK }
            }

            "Artifacts" -> {
                savedList = savedList.filter { it.itemType == ItemType.ARTIFACT }
            }

            "Tours" -> {
                savedList = savedList.filter { it.itemType == ItemType.TOUR }
            }
        }

        _uiState.update { it.copy(displayedSavedList = savedList) }
    }
}