package com.egtourguide.home.domain.model

data class ToursScreenResponse(
    val tours: List<AbstractedTour>,
    val tourTypes: List<String>
)
