package com.egtourguide.home.presentation.searchResults

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.SearchResult
import com.egtourguide.core.domain.usecases.ChangeArtifactSavedStateUseCase
import com.egtourguide.core.domain.usecases.ChangeLandmarkSavedStateUseCase
import com.egtourguide.core.utils.ItemType
import com.egtourguide.home.domain.usecases.SearchUseCase
import com.egtourguide.home.presentation.filter.FilterScreenState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchResultsViewModel @Inject constructor(
    private val searchUseCase: SearchUseCase,
    private val changeLandmarkSavedStateUseCase: ChangeLandmarkSavedStateUseCase,
    private val changeArtifactSavedStateUseCase: ChangeArtifactSavedStateUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchResultsUIState())
    val uiState = _uiState.asStateFlow()

    fun getSearchResults(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchUseCase(query).onResponse(
                onLoading = {
                    _uiState.update {
                        it.copy(
                            isLoading = true,
                            isShowEmptyState = false,
                            isCallSent = true
                        )
                    }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            results = response,
                            displayedResults = response,
                            isShowEmptyState = true
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

    fun onSaveClicked(item: SearchResult) {
        val errorLogic = {
            _uiState.update { it.copy(isSaveError = true) }
            item.isSaved = !item.isSaved
        }

        viewModelScope.launch(Dispatchers.IO) {
            item.isSaved = !item.isSaved
            _uiState.update { it.copy(isSaveCall = item.isSaved) }

            if (item.itemType == ItemType.ARTIFACT) {
                changeArtifactSavedStateUseCase(artifactId = item.id).onResponse(
                    onLoading = {},
                    onSuccess = { _uiState.update { it.copy(isSaveSuccess = true) } },
                    onFailure = { errorLogic() },
                    onNetworkError = errorLogic
                )
            } else {
                changeLandmarkSavedStateUseCase(item.id).onResponse(
                    onLoading = {},
                    onSuccess = { _uiState.update { it.copy(isSaveSuccess = true) } },
                    onFailure = { errorLogic() },
                    onNetworkError = errorLogic
                )
            }
        }
    }

    // TODO: Add rest of filters!!
    fun filterResults(filterState: FilterScreenState) {
        _uiState.update {
            it.copy(
                displayedResults = it.results.filter { result ->
                    result.location in filterState.selectedLocations
                }
            )
        }
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(isSaveError = false) }
    }
}