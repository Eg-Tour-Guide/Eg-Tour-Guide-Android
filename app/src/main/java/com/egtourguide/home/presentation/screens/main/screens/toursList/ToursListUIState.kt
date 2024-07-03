package com.egtourguide.home.presentation.screens.main.screens.toursList

import com.egtourguide.home.domain.model.AbstractedTour

data class ToursListUIState(
    val callIsSent: Boolean = false,
    val isLoading: Boolean = true,
    val tours: List<AbstractedTour> = emptyList(),
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null
)
