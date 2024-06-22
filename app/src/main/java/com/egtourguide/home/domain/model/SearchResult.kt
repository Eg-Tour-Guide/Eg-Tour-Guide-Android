package com.egtourguide.home.domain.model

data class SearchResult(
    val id: String,
    val image: String,
    val name: String,
    val location: String,
    val isSaved: Boolean,
    val isArtifact: Boolean,
    val category: String? = null,
    val material: String? = null,
    val artifactType: String? = null,
    val rating: Float? = null,
    val ratingCount: Int? = null
)
