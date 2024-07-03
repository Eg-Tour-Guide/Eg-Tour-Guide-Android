package com.egtourguide.home.presentation.screens.main.screens.artifactsList

import com.egtourguide.home.domain.model.AbstractedArtifact

data class ArtifactsListUIState(
    val callIsSent: Boolean = false,
    val isLoading: Boolean = true,
    val artifacts: List<AbstractedArtifact> = emptyList(),
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null
)
