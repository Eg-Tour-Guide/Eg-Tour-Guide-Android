package com.egtourguide.home.domain.model

data class SearchResult(
    val id: String,
    val image: String,
    val name: String,
    val location: String,
    val isSaved: Boolean,
    val isLandmark: Boolean,
    val rating: Float? = null,
    val ratingCount: Int? = null
)
