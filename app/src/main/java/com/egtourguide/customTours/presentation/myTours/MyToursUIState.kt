package com.egtourguide.customTours.presentation.myTours

import com.egtourguide.home.domain.model.AbstractedTour

data class MyToursUIState(
    val isLoading: Boolean = true,
    val myTours: List<AbstractedTour> = emptyList(),
    val error: String? = null,
    val isCallSent: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val isShowEmptyState: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null,
)
