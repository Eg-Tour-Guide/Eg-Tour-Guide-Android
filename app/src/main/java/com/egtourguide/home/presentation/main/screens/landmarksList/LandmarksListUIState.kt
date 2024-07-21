package com.egtourguide.home.presentation.main.screens.landmarksList

import com.egtourguide.home.domain.model.AbstractedLandmark
import com.egtourguide.home.domain.model.DetectedArtifact

data class LandmarksListUIState(
    val callIsSent: Boolean = false,
    val isLoading: Boolean = true,
    val landmarks: List<AbstractedLandmark> = emptyList(),
    val displayedLandmarks: List<AbstractedLandmark> = emptyList(),
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null,
    val isDetectionLoading: Boolean = false,
    val detectedArtifact: DetectedArtifact? = null
)
