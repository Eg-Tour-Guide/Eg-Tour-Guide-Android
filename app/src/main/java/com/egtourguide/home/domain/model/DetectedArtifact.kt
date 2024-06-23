package com.egtourguide.home.domain.model

data class DetectedArtifact(
    val id: String,
    val name: String,
    val similarity: Float,
    val message: String
)
