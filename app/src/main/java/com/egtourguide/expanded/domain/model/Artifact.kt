package com.egtourguide.expanded.domain.model

import com.egtourguide.home.domain.model.AbstractedArtifact

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
