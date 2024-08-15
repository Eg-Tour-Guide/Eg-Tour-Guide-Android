package com.egtourguide.home.domain.model

data class ArtifactsScreenResponse(
    val artifacts: List<AbstractedArtifact>,
    val materials: List<String>,
    val artifactTypes: List<String>
)
