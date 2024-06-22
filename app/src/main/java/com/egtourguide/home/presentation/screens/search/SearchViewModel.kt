package com.egtourguide.home.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.usecases.DeleteSearchHistoryUseCase
import com.egtourguide.home.domain.usecases.GetSearchHistoryUseCase
import com.egtourguide.home.domain.usecases.SearchUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchHistoryUseCase: GetSearchHistoryUseCase,
    private val deleteSearchHistoryUseCase: DeleteSearchHistoryUseCase,
    private val getSearchSuggestionsUseCase: SearchUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUIState())
    val uiState = _uiState.asStateFlow()

    fun getSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            getSearchHistoryUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isRecentSearchesLoading = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isRecentSearchesLoading = false,
                            searchHistory = response.reversed().take(5)
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isRecentSearchesLoading = false, error = error) }
                }
            )
        }
    }

    fun getSearchSuggestions() {
        if (uiState.value.searchQuery.isNotEmpty()) {
            viewModelScope.launch(Dispatchers.IO) {
                getSearchSuggestionsUseCase(query = uiState.value.searchQuery).onResponse(
                    onLoading = {
                        _uiState.update { it.copy(isSearchLoading = true) }
                    },
                    onSuccess = { response ->
                        _uiState.update {
                            it.copy(
                                isSearchLoading = false,
                                searchSuggestions = response.map { result -> result.name }
                            )
                        }
                    },
                    onFailure = { error ->
                        _uiState.update { it.copy(isSearchLoading = false, error = error) }
                    }
                )
            }
        }
    }

    fun updateSearchQuery(newQuery: String) {
        if (newQuery.isEmpty()) {
            _uiState.update {
                it.copy(searchSuggestions = emptyList())
            }
            getSearchHistory()
        }
        _uiState.update { it.copy(searchQuery = newQuery) }
    }

    fun clearHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            deleteSearchHistoryUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isClearHistoryLoading = true) }
                },
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            searchHistory = emptyList(),
                            isClearHistoryLoading = false
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update {
                        it.copy(
                            error = error,
                            isClearHistoryLoading = false
                        )
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}