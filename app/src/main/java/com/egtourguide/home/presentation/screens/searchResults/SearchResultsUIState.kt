package com.egtourguide.home.presentation.screens.searchResults

import com.egtourguide.home.domain.model.SearchResult

data class SearchResultsUIState(
    val isLoading: Boolean = false,
    val results: List<SearchResult> = emptyList(),
    val displayedResults: List<SearchResult> = emptyList(),
    val error: String? = null,
    val isCallSent: Boolean = false,
    val isShowEmptyState: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null,
)
