package com.egtourguide.user.presentation.user

import com.egtourguide.home.domain.model.DetectedArtifact

data class UserScreenState(
    val error: String? = null,
    val isDetectionLoading: Boolean = false,
    val detectedArtifact: DetectedArtifact? = null,
    val isDetectionError: Boolean = false,
    val username: String = ""
)
