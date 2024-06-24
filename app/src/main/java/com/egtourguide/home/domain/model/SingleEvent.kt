package com.egtourguide.home.domain.model

data class SingleEvent(
    val id: String,
    val name: String,
    val images: List<String>,
    val description: String,
    val date: String,
    val placeName: String,
    val duration: Int,
    val category: String,
    val latitude: Double,
    val longitude: Double
)
