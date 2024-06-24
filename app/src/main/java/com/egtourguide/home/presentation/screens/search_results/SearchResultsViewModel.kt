package com.egtourguide.home.presentation.screens.search_results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.domain.model.SearchResult
import com.egtourguide.home.domain.usecases.ChangeArtifactSavedStateUseCase
import com.egtourguide.home.domain.usecases.ChangePlaceSavedStateUseCase
import com.egtourguide.home.domain.usecases.SearchUseCase
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
    private val changePlaceSavedStateUseCase: ChangePlaceSavedStateUseCase,
    private val changeArtifactSavedStateUseCase: ChangeArtifactSavedStateUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchResultsUIState())
    val uiState = _uiState.asStateFlow()
    var filters: HashMap<*, *>? = null

    fun getSearchResults(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            var searchQuery = query
            if (searchQuery.isEmpty()) {
                val filterQuery = filters?.get("Query") as List<String>
                searchQuery = filterQuery.first()
            }
            searchUseCase(searchQuery).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, isShowEmptyState = false) }
                },
                onSuccess = { response ->
                    val results = filterSearchResults(results = response, filters = filters)
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            results = results,
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

    fun onSaveClicked(item: SearchResult) {
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

    private fun filterSearchResults(
        results: List<SearchResult>,
        filters: HashMap<*, *>?
    ): List<SearchResult> {
        var resultedList = results
        filters?.forEach { (filterKey, filterValue) ->
            filterKey as String
            filterValue as List<String>

            when (filterKey) {
                "Category" -> {
                    resultedList = resultedList.filter { item ->
                        filterValue.contains(item.category)
                    }
                }
            }
        }
        return resultedList
    }

    fun clearSaveSuccess() {
        _uiState.update { it.copy(isSaveSuccess = false) }
    }

    fun clearSaveError() {
        _uiState.update { it.copy(saveError = null) }
    }
}