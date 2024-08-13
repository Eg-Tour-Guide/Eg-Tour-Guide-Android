package com.egtourguide.home.presentation.main.screens.toursList

import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.domain.model.DetectedArtifact

data class ToursListUIState(
    val callIsSent: Boolean = false,
    val isLoading: Boolean = true,
    val isNetworkError: Boolean = false,
    val isRefreshing: Boolean = false,
    val error: String? = null,
    val tours: List<AbstractedTour> = emptyList(),
    val displayedTours: List<AbstractedTour> = emptyList(),
    val isSaveSuccess: Boolean = false,
    val isSaveCall: Boolean = true,
    val isSaveError: Boolean = false,
    val isDetectionLoading: Boolean = false,
    val detectedArtifact: DetectedArtifact? = null
)
