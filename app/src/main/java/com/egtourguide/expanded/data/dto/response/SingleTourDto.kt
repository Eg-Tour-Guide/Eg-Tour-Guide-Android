package com.egtourguide.expanded.data.dto.response

import com.egtourguide.home.data.dto.response.ToursListDto
import com.egtourguide.expanded.domain.model.SingleTour

data class SingleTourDto(
    val status: String,
    val tour: TourDto,
    val relatedTours: List<ToursListDto.Tour>?
) {
    fun toTour() = SingleTour(
        id = tour._id,
        name = tour.name,
        images = tour.images,
        description = tour.description,
        ratingAverage = tour.ratingAverage ?: 0.0,
        ratingQuantity = tour.ratingQuantity ?: 0,
        saved = tour.saved,
        reviews = tour.reviews.map { it.toReview() },
        relatedTours = relatedTours.orEmpty().map { it.toDomainAbstractedTour() },
        type = tour.type ?: "",
        duration = tour.duration
    )
}

data class TourDto(
    val _id: String,
    val name: String,
    val images: List<String>,
    val description: String,
    val ratingAverage: Double?,
    val ratingQuantity: Int?,
    val saved: Boolean,
    val reviews: List<ReviewDto>,
    val type: String?,
    val duration: Int
)