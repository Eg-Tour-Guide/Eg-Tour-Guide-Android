package com.egtourguide.home.presentation.screens.saved_items

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.SavedItem
import com.egtourguide.home.domain.usecases.ChangeArtifactSavedStateUseCase
import com.egtourguide.home.domain.usecases.ChangePlaceSavedStateUseCase
import com.egtourguide.home.domain.usecases.ChangeTourSavedStateUseCase
import com.egtourguide.home.domain.usecases.GetSavedItemsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.HashMap
import javax.inject.Inject

@HiltViewModel
class SavedViewModel @Inject constructor(
    private val getSavedItemsUseCase: GetSavedItemsUseCase,
    private val changePlaceSavedStateUseCase: ChangePlaceSavedStateUseCase,
    private val changeTourSavedStateUseCase: ChangeTourSavedStateUseCase,
    private val changeArtifactSavedStateUseCase: ChangeArtifactSavedStateUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SavedScreenUIState())
    val uiState = _uiState.asStateFlow()
    var filters: HashMap<*, *>? = null

    fun getSavedItems() {
        viewModelScope.launch(Dispatchers.IO) {
            getSavedItemsUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, isShowEmptyState = false) }
                },
                onSuccess = { response ->
                    val results = filterSearchResults(results = response, filters = filters)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            savedList = results,
                            isShowEmptyState = true
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                }
            )
        }
    }

    fun onSaveClicked(item: SavedItem) {
        viewModelScope.launch(Dispatchers.IO) {
            if (item.isArtifact) {
                item.isSaved = !item.isSaved
                changeArtifactSavedStateUseCase(
                    artifactId = item.id
                ).onResponse(
                    onLoading = {},
                    onSuccess = {
                        _uiState.update { it.copy(isSaveSuccess = true, isSave = item.isSaved) }
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(saveError = error) }
                    }
                )
            } else if (item.isTour) {
                item.isSaved = !item.isSaved
                changeTourSavedStateUseCase(item.id).onResponse(
                    onLoading = {},
                    onSuccess = {
                        _uiState.update { it.copy(isSaveSuccess = true, isSave = item.isSaved) }
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(saveError = error) }
                    }
                )
            } else {
                item.isSaved = !item.isSaved
                changePlaceSavedStateUseCase(item.id).onResponse(
                    onLoading = {},
                    onSuccess = {
                        _uiState.update { it.copy(isSaveSuccess = true, isSave = item.isSaved) }
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(saveError = error) }
                    }
                )
            }
        }
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(saveError = null) }
    }

    private fun filterSearchResults(
        results: List<SavedItem>,
        filters: HashMap<*, *>?
    ): List<SavedItem> {
        var resultedList = results
        filters?.forEach { (filterKey, filterValue) ->
            filterKey as String
            filterValue as List<String>

            when (filterKey) {
                "Category" -> {
                    if(filterValue.isNotEmpty()){
                        resultedList = resultedList.filter { item ->
                            when (filterValue.first()) {
                                "Tours" -> item.isTour
                                "Landmarks" -> !item.isArtifact && !item.isTour
                                "Artifacts" -> item.isArtifact
                                else -> true
                            }
                        }
                    }
                }
            }
        }
        return resultedList
    }
}