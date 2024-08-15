package com.egtourguide.home.domain.model

data class AbstractedLandmark(
    val id: String,
    val name: String,
    val image: String,
    val location: String,
    val category: String,
    var isSaved: Boolean,
    val rating: Double,
    val ratingCount: Int
)
