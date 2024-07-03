package com.egtourguide.expanded.data.dto.response

import com.egtourguide.expanded.domain.model.SingleEvent

data class SingleEventDto(
    val status: String,
    val event: EventDto
) {
    fun toSingleEvent() = SingleEvent(
        id = event._id,
        name = event.name,
        images = event.images,
        description = event.description,
        date = event.sDate,
        placeName = event.placeName,
        duration = event.duration,
        category = event.category,
        latitude = event.location.coordinates[0],
        longitude = event.location.coordinates[1]
    )
}

data class EventDto(
    val location: Location,
    val _id: String,
    val name: String,
    val images: List<String>,
    val description: String,
    val sDate: String,
    val placeName: String,
    val duration: Int,
    val category: String,
    val __v: Int
)