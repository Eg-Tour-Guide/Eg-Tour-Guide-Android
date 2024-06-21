package com.egtourguide.home.domain.model

data class Artifact(
    val id: String,
    val title: String,
    val images: List<String>,
    val description: String,
    val museum: String,
    val type: String,
    val material: String,
    val saved: Boolean,
    val relatedArtifacts: List<AbstractedArtifact>,
    val arModel: String
)
