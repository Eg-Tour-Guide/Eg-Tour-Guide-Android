package com.egtourguide.home.presentation.screens.main.screens.toursList

import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.domain.model.DetectedArtifact

data class ToursListUIState(
    val callIsSent: Boolean = false,
    val isLoading: Boolean = true,
    val error: String? = null,
    val tours: List<AbstractedTour> = emptyList(),
    val displayedTours: List<AbstractedTour> = emptyList(),
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null,
    val isDetectionLoading: Boolean = false,
    val detectedArtifact: DetectedArtifact? = null
)
