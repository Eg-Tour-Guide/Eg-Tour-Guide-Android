package com.egtourguide.home.domain.model

// TODO: Change model!!
data class TourDetails(
    val startDate: Long,
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