package com.egtourguide.home.presentation.screens.main.screens.landmarks_list

import com.egtourguide.home.domain.model.Place

data class LandmarksListUIState(
    val callIsSent: Boolean = false,
    val isLoading: Boolean = true,
    val landmarks: List<Place> = emptyList(),
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null
)
