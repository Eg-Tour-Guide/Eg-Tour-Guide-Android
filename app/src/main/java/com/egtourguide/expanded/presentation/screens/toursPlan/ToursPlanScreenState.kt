package com.egtourguide.expanded.presentation.screens.toursPlan

import com.egtourguide.expanded.domain.model.TourDetailsPlace

data class ToursPlanScreenState(
    val isLoading: Boolean = false,
    val isNetworkError: Boolean = false,
    val errorMessage: String? = null,
    val id: String = "",
    val title: String = "",
    val days: Map<Int, List<TourDetailsPlace>> = emptyMap(),
    val callIsSent: Boolean = false,
    val chosenDay: Int = 1
)
