package com.egtourguide.home.presentation.search

data class SearchUIState(
    val isLoading: Boolean = false,
    val isCallSent: Boolean = false,
    val isNetworkError: Boolean = false,
    val isClearHistoryLoading: Boolean = false,
    val searchQuery: String = "",
    val isError: Boolean = false,
    val searchHistory: List<String> = emptyList()
)
