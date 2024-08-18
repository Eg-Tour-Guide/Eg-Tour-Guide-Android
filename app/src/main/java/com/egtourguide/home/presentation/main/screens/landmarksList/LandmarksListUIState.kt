package com.egtourguide.home.presentation.main.screens.landmarksList

import com.egtourguide.home.domain.model.AbstractedLandmark
import com.egtourguide.home.domain.model.DetectedArtifact

data class LandmarksListUIState(
    val callIsSent: Boolean = false,
    val isNetworkError: Boolean = false,
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val refreshFilters: Boolean = false,
    val landmarks: List<AbstractedLandmark> = emptyList(),
    val displayedLandmarks: List<AbstractedLandmark> = emptyList(),
    val error: String? = null,
    val isSaveCall: Boolean = true,
    val isSaveSuccess: Boolean = false,
    val isSaveError: Boolean = false,
    val isDetectionLoading: Boolean = false,
    val isDetectionError: Boolean = false,
    val detectedArtifact: DetectedArtifact? = null
)
