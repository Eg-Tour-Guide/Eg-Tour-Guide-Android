package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.AbstractedLandmark
import com.egtourguide.home.domain.model.LandmarksScreenResponse

data class LandmarksListDto(
    val places: List<PlaceDto>,
    val status: String,
    val filter: Filter
) {
    fun toLandmarksResponse() = LandmarksScreenResponse(
        landmarks = places.map { it.toDomainPlace() },
        tourismTypes = filter.category,
        locations = filter.location
    )

    data class PlaceDto(
        val _id: String,
        val govName: String,
        val image: String,
        val name: String,
        val category: String,
        val ratingAverage: Double,
        val ratingQuantity: Int,
        val saved: Boolean
    ) {
        fun toDomainPlace(): AbstractedLandmark {
            return AbstractedLandmark(
                id = _id,
                name = name,
                location = govName,
                category = category,
                image = image,
                isSaved = saved,
                rating = ratingAverage,
                ratingCount = ratingQuantity
            )
        }
    }

    data class Filter(
        val category: List<String>,
        val location: List<String>
    )
}