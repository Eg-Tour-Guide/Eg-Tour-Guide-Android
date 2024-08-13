package com.egtourguide.home.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.egtourguide.core.utils.onResponse
import com.egtourguide.home.domain.usecases.DeleteSearchHistoryUseCase
import com.egtourguide.home.domain.usecases.GetSearchHistoryUseCase
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
    private val deleteSearchHistoryUseCase: DeleteSearchHistoryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(SearchUIState())
    val uiState = _uiState.asStateFlow()

    fun getSearchHistory() {
        viewModelScope.launch(Dispatchers.IO) {
            getSearchHistoryUseCase().onResponse(
                onLoading = {
                    _uiState.update { it.copy(isLoading = true, isCallSent = true) }
                },
                onSuccess = { response ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            searchHistory = response.reversed().take(10)
                        )
                    }
                },
                onFailure = {
                    _uiState.update { it.copy(isLoading = false) }
                },
                onNetworkError = {
                    _uiState.update { it.copy(isLoading = false, isNetworkError = true) }
                }
            )
        }
    }

    fun updateSearchQuery(newQuery: String) {
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
                onFailure = {
                    _uiState.update { it.copy(isClearHistoryLoading = false, isError = true) }
                },
                onNetworkError = {
                    _uiState.update {
                        it.copy(
                            isClearHistoryLoading = false,
                            isNetworkError = true
                        )
                    }
                }
            )
        }
    }

    fun clearError() {
        _uiState.update { it.copy(isError = false) }
    }

    fun clearNetworkError() {
        _uiState.update { it.copy(isNetworkError = false) }
    }
}