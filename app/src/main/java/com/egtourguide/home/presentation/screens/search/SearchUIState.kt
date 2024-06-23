package com.egtourguide.home.presentation.screens.search

data class SearchUIState(
    val isSearchLoading: Boolean = false,
    val isRecentSearchesLoading: Boolean = true,
    val isClearHistoryLoading: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null,
    val searchSuggestions: List<String> = emptyList(),
    val searchHistory: List<String> = emptyList()
)
