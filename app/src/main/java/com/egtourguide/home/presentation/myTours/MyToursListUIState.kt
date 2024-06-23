package com.egtourguide.home.presentation.myTours

import com.egtourguide.home.domain.model.AbstractedTour

data class MyToursListUIState(
    val isLoading: Boolean = true,
    val tours: List<AbstractedTour> = emptyList(),
    val errorMessage: String? = null
)
