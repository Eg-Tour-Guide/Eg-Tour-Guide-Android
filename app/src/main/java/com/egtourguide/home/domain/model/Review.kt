package com.egtourguide.home.domain.model

data class Review(
    val id: String,
    val authorName: String,
    val authorImage: String,
    val rating: Int,
    val description: String
)