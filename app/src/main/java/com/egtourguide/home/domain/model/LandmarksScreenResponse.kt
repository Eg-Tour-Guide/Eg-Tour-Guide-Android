package com.egtourguide.home.domain.model

data class LandmarksScreenResponse(
    val landmarks: List<AbstractedLandmark>,
    val tourismTypes: List<String>,
    val locations: List<String>
)
