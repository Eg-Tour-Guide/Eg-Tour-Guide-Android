package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.TourDetails
import com.egtourguide.home.domain.model.TourDetailsPlace

// TODO: Change model!!
data class TourDetailsDto(
    val status: String,
    val details: List<Detail>
) {
    fun toTourDetails() = TourDetails(
        startDate = 0,
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
    val _id: String,
    val name: String,
    val govName: String,
    val images: List<String>,
    val description: String,
    val location: Location,
    val category: String,
    val ratingAverage: Double,
    val ratingQuantity: Int,
    val __v: Int,
    val type: String,
    val vrModel: String?
) {
    fun toTourDetailsPlace() = TourDetailsPlace(
        id = _id,
        image = images[0],
        title = name,
        govName = govName,
        ratingAverage = ratingAverage,
        ratingQuantity = ratingQuantity,
        duration = 3
    )
}

data class Location(
    val coordinates: List<Double>
)