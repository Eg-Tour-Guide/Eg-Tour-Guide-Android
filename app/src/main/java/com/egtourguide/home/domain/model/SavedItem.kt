package com.egtourguide.home.domain.model

data class SavedItem(
    val id: String,
    val image: String,
    val name: String,
    val location: String? = null,
    val duration: Int? = null,
    var isSaved: Boolean = true,
    val isArtifact: Boolean = false,
    val isTour: Boolean = false,
    val category: String? = null,
    val material: String? = null,
    val artifactType: String? = null,
    val rating: Float? = null,
    val ratingCount: Int? = null
)
