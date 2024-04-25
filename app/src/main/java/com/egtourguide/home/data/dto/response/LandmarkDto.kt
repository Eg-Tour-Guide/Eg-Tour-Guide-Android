package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.Landmark

data class LandmarkDto(
    val id: String
) {
    fun toLandmark() = Landmark(
        id = id
    )
}
