package com.egtourguide.home.presentation.searchResults

import com.egtourguide.home.domain.model.SearchResult

data class SearchResultsUIState(
    val isLoading: Boolean = false,
    val isNetworkError: Boolean = false,
    val isRefreshing: Boolean = false,
    val results: List<SearchResult> = emptyList(),
    val displayedResults: List<SearchResult> = emptyList(),
    val error: String? = null,
    val isCallSent: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val isSaveCall: Boolean = true,
    val isSaveError: Boolean = false
)
