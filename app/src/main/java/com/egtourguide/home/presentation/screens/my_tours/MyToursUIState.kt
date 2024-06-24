package com.egtourguide.home.presentation.screens.my_tours

data class MyToursUIState(
    val isLoading: Boolean = true,
    val myTours: List<Unit> = emptyList(),
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null,
)
