package com.egtourguide.home.presentation.screens.main.screens.landmarksList

import com.egtourguide.home.domain.model.AbstractedLandmark

data class LandmarksListUIState(
    val callIsSent: Boolean = false,
    val isLoading: Boolean = true,
    val landmarks: List<AbstractedLandmark> = emptyList(),
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null
)
