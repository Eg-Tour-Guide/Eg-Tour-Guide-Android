package com.egtourguide.home.presentation.screens.search_results

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.model.SearchResult
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
    private val searchUseCase: SearchUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SearchResultsUIState())
    val uiState = _uiState.asStateFlow()

    fun getSearchResults(query: String) {
        viewModelScope.launch(Dispatchers.IO) {
            searchUseCase(query).onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true) }
                },
                onSuccess = { response ->
                    _uiState.update { it.copy(isLoading = false, results = response) }
                },
                onFailure = { error ->
                    _uiState.update { it.copy(isLoading = false, error = error) }
                }
            )
        }
    }

    fun onSaveClicked(item: SearchResult) {

    }
}