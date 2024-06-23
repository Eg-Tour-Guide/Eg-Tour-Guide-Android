package com.egtourguide.home.presentation.screens.toursPlan

import com.egtourguide.home.domain.model.TourDetailsPlace

data class ToursPlanScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val id: String = "",
    val title: String = "",
    val days: Map<Int, List<TourDetailsPlace>> = emptyMap(), // TODO: Change model!!
    val startDate: Long = 0,
    val chosenDate: Long = 0,
    val callIsSent: Boolean = false, // TODO: Check if not needed!!
    val showDatePicker: Boolean = false,
    val chosenDay: Int = 1
)
