package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.Landmark
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.domain.model.Review

// TODO: Add included artifacts!!
data class SingleLandmarkDto(
    val status: String,
    val place: LandmarkDto,
    val relatedPlaces: List<RelatedPlaceDto>
) {
    fun toLandmark() = Landmark(
        id = place._id,
        title = place.name,
        images = place.images,
        location = place.govName,
        description = place.description,
        latitude = place.location.coordinates[0],
        longitude = place.location.coordinates[1],
        type = place.type,
        saved = place.saved,
        reviewsAverage = place.ratingAverage,
        reviewsCount = place.ratingQuantity,
        reviews = place.reviews.map { it.toReview() },
        relatedPlaces = relatedPlaces.map { it.toPlace() }

    )
}

data class LandmarkDto(
    val _id: String,
    val name: String,
    val images: List<String>,
    val govName: String,
    val description: String,
    val location: LocationDto,
    val type: String,
    val saved: Boolean,
    val ratingAverage: Double,
    val ratingQuantity: Int,
    val reviews: List<ReviewDto>
)

data class LocationDto(
    val coordinates: List<Double>
)

data class ReviewDto(
    val _id: String,
    val review: String,
    val rating: Int,
    val place: String,
    val user: UserDto,
    val __v: Int,
    val id: String
) {
    fun toReview() = Review(
        id = _id,
        rating = rating,
        authorName = user.username,
        authorImage = user.photo,
        description = review,
    )
}

data class UserDto(
    val _id: String,
    val username: String,
    val photo: String
)

data class RelatedPlaceDto(
    val _id: String,
    val name: String,
    val image: String,
    val govName: String,
    val ratingAverage: Float,
    val ratingQuantity: Int,
    val saved: Boolean
) {
    fun toPlace() = Place(
        id = _id,
        name = name,
        image = image,
        location = govName,
        isSaved = saved,
        rating = ratingAverage,
        ratingCount = ratingQuantity
    )
}