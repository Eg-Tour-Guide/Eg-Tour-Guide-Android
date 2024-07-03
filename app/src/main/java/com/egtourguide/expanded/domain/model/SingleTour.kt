package com.egtourguide.expanded.domain.model

import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.domain.model.Review

data class SingleTour(
    val id: String,
    val name: String,
    val images: List<String>,
    val description: String,
    val ratingAverage: Double,
    val ratingQuantity: Int,
    val saved: Boolean,
    val reviews: List<Review>,
    val relatedTours: List<AbstractedTour>,
    val type: String,
    val duration: Int
)
