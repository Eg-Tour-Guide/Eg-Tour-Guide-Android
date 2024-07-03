package com.egtourguide.expanded.data.dto.response

import com.egtourguide.expanded.domain.model.TourDetails
import com.egtourguide.expanded.domain.model.TourDetailsPlace

data class TourDetailsDto(
    val status: String,
    val name: String,
    val details: List<Detail>
) {
    fun toTourDetails() = TourDetails(
        name = name,
        days = details.associate {
            it.day to it.places.map { place ->
                place.toTourDetailsPlace()
            }
        }
    )
}

data class Detail(
    val places: List<Place>,
    val day: Int
)

data class Place(
    val placeDetails: PlaceDetails,
    val time: Int
) {
    fun toTourDetailsPlace() = TourDetailsPlace(
        id = placeDetails._id,
        image = placeDetails.images[0],
        title = placeDetails.name,
        govName = placeDetails.govNAme,
        ratingAverage = placeDetails.ratingAverage,
        ratingQuantity = placeDetails.ratingQuantity,
        duration = time

    )
}

data class PlaceDetails(
    val _id: String,
    val name: String,
    val govNAme: String,
    val images: List<String>,
    val ratingAverage: Double,
    val ratingQuantity: Int
)

data class Location(
    val coordinates: List<Double>
)