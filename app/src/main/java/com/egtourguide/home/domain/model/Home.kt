package com.egtourguide.home.domain.model

import com.egtourguide.home.data.dto.response.HomeDto

data class Home(
    val event: List<Event>,
    val explore: List<Place>,
    val recentlyAdded: List<Place>,
    val recentlyViewed: List<Place>,
    val suggestedForYou: List<Place>,
    val topRated: List<Place>
)
