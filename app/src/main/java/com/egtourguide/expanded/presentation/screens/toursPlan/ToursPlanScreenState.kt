package com.egtourguide.expanded.presentation.screens.toursPlan

import com.egtourguide.expanded.domain.model.TourDetailsPlace

data class ToursPlanScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val id: String = "",
    val title: String = "",
    val days: Map<Int, List<TourDetailsPlace>> = emptyMap(), // TODO: Change model!!
    val callIsSent: Boolean = false,
    val showDatePicker: Boolean = false,
    val chosenDay: Int = 1
)
