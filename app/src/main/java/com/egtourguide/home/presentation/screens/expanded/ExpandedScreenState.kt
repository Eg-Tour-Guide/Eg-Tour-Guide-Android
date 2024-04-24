package com.egtourguide.home.presentation.screens.expanded

import com.egtourguide.home.domain.model.Review

// TODO: Edit expanded state!!
data class ExpandedScreenState(
    val images: List<String> = emptyList(),
    val title: String = "",
    val reviewsAverage: Double = 0.0,
    val reviews: List<Review> = emptyList(),
    val tourismTypes: List<String> = emptyList(),
    val isSaved: Boolean = false,
    val description: String = "",
    val location: String = ""
)