package com.egtourguide.home.presentation.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
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
    private val getSearchSuggestionsUseCase: SearchUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchUIState())
    val uiState = _uiState.asStateFlow()

    fun getSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            getSearchHistoryUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true) }
                },
                onSuccess = { response ->
                    _uiState.update { it.copy(isLoading = false, searchHistory = response) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                }
            )
        }
    }

    fun getSearchSuggestions() {
        viewModelScope.launch(Dispatchers.IO) {
            getSearchSuggestionsUseCase(query = uiState.value.searchQuery).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            searchSuggestions = response.map { result -> result.name }
                        )
                    }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                }
            )
        }
    }

    fun updateSearchQuery(newQuery: String) {
        _uiState.update { it.copy(searchQuery = newQuery) }
    }

    fun clearHistory() {

    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}