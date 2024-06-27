package com.egtourguide.home.presentation.screens.main.screens.artifacts_list

import com.egtourguide.home.domain.model.AbstractedArtifact

data class ArtifactsListUIState(
    val isLoading: Boolean = true,
    val artifacts: List<AbstractedArtifact> = emptyList(),
    val error: String? = null,
    val isSaveSuccess: Boolean = false,
    val isShowEmptyState: Boolean = false,
    val isSave: Boolean = true,
    val saveError: String? = null,
)
