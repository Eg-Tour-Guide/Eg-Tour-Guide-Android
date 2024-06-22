package com.egtourguide.home.presentation.screens.search

data class SearchUIState(
    val isLoading: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null,
    val searchSuggestions: List<String> = emptyList(),
    val searchHistory: List<String> = emptyList()
)
