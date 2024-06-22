package com.egtourguide.home.domain.model

data class Place(
    val id: String,
    val name: String,
    val image: String,
    val category: String? = null,
    val location: String,
    var isSaved: Boolean,
    val rating: Float,
    val ratingCount: Int
)
