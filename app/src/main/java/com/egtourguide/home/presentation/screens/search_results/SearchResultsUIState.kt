package com.egtourguide.home.presentation.screens.search_results

import com.egtourguide.home.domain.model.SearchResult

data class SearchResultsUIState(
    val isLoading: Boolean = true,
    val results: List<SearchResult> = emptyList(),
    val displayedResults: List<SearchResult> = emptyList(),
    val error: String? = null,
    val isShowEmptyState: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null,
)
