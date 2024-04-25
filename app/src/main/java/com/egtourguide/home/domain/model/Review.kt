package com.egtourguide.home.domain.model

data class Review(
    val authorName: String,
    val authorImage: String,
    val rating: Double,
    val description: String
)
