package com.egtourguide.home.presentation.screens.main.screens.tours_list

import com.egtourguide.home.domain.model.AbstractedTour

data class ToursListUIState(
    val isLoading: Boolean = true,
    val tours: List<AbstractedTour> = emptyList(),
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
    val isShowEmptyState: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null,
)
