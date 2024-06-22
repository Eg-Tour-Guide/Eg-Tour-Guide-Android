package com.egtourguide.home.domain.model

data class AbstractedTour(
    val id: String,
    val image: String,
    val title: String,
    val duration: Int,
    val rating: Float,
    val ratingCount: Int,
    val isSaved: Boolean
)
