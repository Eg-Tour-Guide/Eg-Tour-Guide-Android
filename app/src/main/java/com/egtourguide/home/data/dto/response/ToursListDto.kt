package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.AbstractedTour

data class ToursListDto(
    val status: String,
    val tours: List<Tour>
) {
    data class Tour(
        val _id: String,
        val duration: Int,
        val image: String?,
        val name: String,
        val ratingAverage: Double,
        val ratingQuantity: Int,
        val saved: Boolean
    ) {
        fun toDomainAbstractedTour(): AbstractedTour {
            return AbstractedTour(
                id = _id,
                image = image ?: "",
                title = name,
                duration = duration,
                rating = ratingAverage,
                ratingCount = ratingQuantity,
                isSaved = saved
            )
        }
    }
}