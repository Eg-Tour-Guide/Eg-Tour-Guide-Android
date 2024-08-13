package com.egtourguide.customTours.presentation.myTours

import com.egtourguide.home.domain.model.AbstractedTour

data class MyToursUIState(
    val isLoading: Boolean = true,
    val isNetworkError: Boolean = false,
    val myTours: List<AbstractedTour> = emptyList(),
    val error: String? = null,
    val isCallSent: Boolean = false,
    val isSaveSuccess: Boolean = false,
    val isShowEmptyState: Boolean = false,
    val isSaveCall: Boolean = true,
    val isSaveError: Boolean = false
)
