package com.egtourguide.home.presentation.screens.moreReviews

import com.egtourguide.home.domain.model.Review

data class MoreReviewsScreenState(
    val reviews: List<Review> = emptyList(),
    val reviewsAverage: Double = 0.0,
    val reviewsTotal: Int = 0,
)
