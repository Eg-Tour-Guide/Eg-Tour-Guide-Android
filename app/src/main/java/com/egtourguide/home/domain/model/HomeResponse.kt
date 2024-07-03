package com.egtourguide.home.domain.model

data class HomeResponse(
    val event: List<AbstractedEvent>,
    val explore: List<AbstractedLandmark>,
    val recentlyAdded: List<AbstractedLandmark>,
    val recentlyViewed: List<AbstractedLandmark>,
    val suggestedForYou: List<AbstractedLandmark>,
    val topRated: List<AbstractedLandmark>
)
