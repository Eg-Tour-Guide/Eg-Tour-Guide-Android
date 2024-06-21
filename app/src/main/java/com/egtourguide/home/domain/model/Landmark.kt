package com.egtourguide.home.domain.model

data class Landmark(
    val id: String,
    val title: String,
    val images: List<String>,
    val location: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val type: String,
    val saved: Boolean,
    val reviews: List<Review>,
    val relatedPlaces: List<Place>
)
