package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.Place

data class LandmarksListDto(
    val places: List<PlaceDto>,
    val status: String
) {
    data class PlaceDto(
        val _id: String,
        val govName: String,
        val image: String,
        val name: String,
        val category: String,
        val ratingAverage: Int,
        val ratingQuantity: Int,
        val saved: Boolean
    ) {
        fun toDomainPlace(): Place {
            return Place(
                id = _id,
                name = name,
                location = govName,
                image = image,
                category = category,
                isSaved = saved,
                rating = ratingAverage.toFloat(),
                ratingCount = ratingQuantity
            )
        }
    }
}