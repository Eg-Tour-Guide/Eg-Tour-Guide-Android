package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.domain.model.ToursScreenResponse

data class ToursListDto(
    val status: String,
    val tours: List<Tour>,
    val filter: Filter
) {
    fun toToursResponse() = ToursScreenResponse(
        tours = tours.map { it.toDomainAbstractedTour() },
        tourTypes = filter.type
    )

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

    data class Filter(
        val type: List<String>
    )
}