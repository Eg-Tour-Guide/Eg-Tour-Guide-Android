package com.egtourguide.home.presentation.screens.search

data class SearchUIState(
    val isLoading: Boolean = false,
    val isCallSent: Boolean = false,
    val isClearHistoryLoading: Boolean = false,
    val searchQuery: String = "",
    val error: String? = null,
    val searchHistory: List<String> = emptyList()
)
