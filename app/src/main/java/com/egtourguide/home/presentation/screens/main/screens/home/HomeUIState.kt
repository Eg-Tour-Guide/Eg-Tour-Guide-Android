package com.egtourguide.home.presentation.screens.main.screens.home

import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.domain.model.Event
import com.egtourguide.home.domain.model.Place

data class HomeUIState(
    val callIsSent: Boolean = false,
    val isLoading: Boolean = true,
    val isDetectionLoading: Boolean = false,
    val detectedArtifact: DetectedArtifact? = null,
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val error: String? = null,
    val saveError: String? = null,
    val events: List<Event> = emptyList(),
    val suggestedPlaces: List<Place> = emptyList(),
    val topRatedPlaces: List<Place> = emptyList(),
    val explorePlaces: List<Place> = emptyList(),
    val recentlyAddedPlaces: List<Place> = emptyList(),
    val recentlyViewedPlaces: List<Place> = emptyList()
)
