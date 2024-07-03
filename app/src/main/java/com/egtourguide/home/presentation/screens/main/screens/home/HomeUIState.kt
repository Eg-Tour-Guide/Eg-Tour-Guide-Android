package com.egtourguide.home.presentation.screens.main.screens.home

import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.domain.model.AbstractedEvent
import com.egtourguide.home.domain.model.AbstractedLandmark

data class HomeUIState(
    val callIsSent: Boolean = false,
    val isLoading: Boolean = true,
    val isDetectionLoading: Boolean = false,
    val detectedArtifact: DetectedArtifact? = null,
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val error: String? = null,
    val saveError: String? = null,
    val events: List<AbstractedEvent> = emptyList(),
    val suggestedPlaces: List<AbstractedLandmark> = emptyList(),
    val topRatedPlaces: List<AbstractedLandmark> = emptyList(),
    val explorePlaces: List<AbstractedLandmark> = emptyList(),
    val recentlyAddedPlaces: List<AbstractedLandmark> = emptyList(),
    val recentlyViewedPlaces: List<AbstractedLandmark> = emptyList()
)
