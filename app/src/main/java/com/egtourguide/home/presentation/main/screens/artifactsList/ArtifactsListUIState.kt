package com.egtourguide.home.presentation.main.screens.artifactsList

import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.domain.model.DetectedArtifact

data class ArtifactsListUIState(
    val callIsSent: Boolean = false,
    val isLoading: Boolean = true,
    val isNetworkError: Boolean = false,
    val isRefreshing: Boolean = false,
    val artifacts: List<AbstractedArtifact> = emptyList(),
    val displayedArtifacts: List<AbstractedArtifact> = emptyList(),
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
    val isSaveCall: Boolean = true,
    val isSaveError: Boolean = false,
    val isDetectionLoading: Boolean = false,
    val detectedArtifact: DetectedArtifact? = null
)
