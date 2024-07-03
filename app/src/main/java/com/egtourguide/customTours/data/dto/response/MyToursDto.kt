package com.egtourguide.customTours.data.dto.response

import com.egtourguide.home.domain.model.AbstractedTour

data class MyToursDto(
    val status: String,
    val tours: List<Tour>
) {
    data class Tour(
        val _id: String,
        val duration: Int,
        val image: String,
        val name: String,
        val ratingAverage: Int,
        val ratingQuantity: Int,
        val saved: Boolean
    ) {
        fun toDomainAbstractedTour(): AbstractedTour {
            return AbstractedTour(
                id = _id,
                title = name,
                image = image,
                duration = duration,
                ratingCount = ratingQuantity,
                rating = ratingAverage.toFloat(),
                isSaved = saved
            )
        }
    }
}