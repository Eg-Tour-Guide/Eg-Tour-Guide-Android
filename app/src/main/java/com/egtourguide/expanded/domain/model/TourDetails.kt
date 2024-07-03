package com.egtourguide.expanded.domain.model

data class TourDetails(
    val name: String,
    val days: Map<Int, List<TourDetailsPlace>>
)

data class TourDetailsPlace(
    val id: String,
    val image: String,
    val title: String,
    val govName: String,
    val ratingAverage: Double,
    val ratingQuantity: Int,
    val duration: Int
)