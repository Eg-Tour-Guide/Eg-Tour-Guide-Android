package com.egtourguide.home.presentation.screens.expanded

// TODO: Edit expanded state!!
data class ExpandedScreenState(
    val images: List<String> = emptyList(),
    val title: String = "",
    val reviewsAverage: Double = 0.0,
    val reviews: List<String> = emptyList(),
    val tourismType: List<String> = emptyList(),
    val isSaved: Boolean = false,
    val description: String = ""
)