package com.egtourguide.home.data.dto.response

import com.egtourguide.home.domain.model.AbstractedEvent
import com.egtourguide.home.domain.model.HomeResponse
import com.egtourguide.home.domain.model.AbstractedLandmark

data class HomeDto(
    val event: List<ResponseEvent>,
    val explore: List<Explore>,
    val recentlyAdded: List<RecentlyAdded>,
    val recentlyViewed: List<RecentlyViewed>,
    val suggestedForYou: List<SuggestedForYou>,
    val topRated: List<TopRated>
) {
    data class ResponseEvent(
        val _id: String,
        val images: List<Any>,
        val name: String
    ) {
        fun toDomainEvent(): AbstractedEvent {
            return AbstractedEvent(
                id = _id,
                images = images,
                name = name
            )
        }
    }

    data class Explore(
        val _id: String,
        val govName: String,
        val image: String,
        val name: String,
        val ratingAverage: Int,
        val ratingQuantity: Int,
        val saved: Boolean
    ) {
        fun toPlace(): AbstractedLandmark {
            return AbstractedLandmark(
                id = _id,
                name = name,
                image = image,
                location = govName,
                isSaved = saved,
                rating = ratingAverage.toFloat(),
                ratingCount = ratingQuantity
            )
        }
    }

    data class RecentlyAdded(
        val _id: String,
        val govName: String,
        val image: String,
        val name: String,
        val ratingAverage: Int,
        val ratingQuantity: Int,
        val saved: Boolean
    ) {
        fun toPlace(): AbstractedLandmark {
            return AbstractedLandmark(
                id = _id,
                name = name,
                image = image,
                location = govName,
                isSaved = saved,
                rating = ratingAverage.toFloat(),
                ratingCount = ratingQuantity
            )
        }
    }

    data class RecentlyViewed(
        val _id: String,
        val govName: String,
        val image: String,
        val name: String,
        val ratingAverage: Int,
        val ratingQuantity: Int,
        val saved: Boolean
    ) {
        fun toPlace(): AbstractedLandmark {
            return AbstractedLandmark(
                id = _id,
                name = name,
                image = image,
                location = govName,
                isSaved = saved,
                rating = ratingAverage.toFloat(),
                ratingCount = ratingQuantity
            )
        }
    }

    data class SuggestedForYou(
        val _id: String,
        val govName: String,
        val image: String,
        val name: String,
        val ratingAverage: Int,
        val ratingQuantity: Int,
        val saved: Boolean
    ) {
        fun toPlace(): AbstractedLandmark {
            return AbstractedLandmark(
                id = _id,
                name = name,
                image = image,
                location = govName,
                isSaved = saved,
                rating = ratingAverage.toFloat(),
                ratingCount = ratingQuantity
            )
        }
    }

    data class TopRated(
        val _id: String,
        val govName: String,
        val image: String,
        val name: String,
        val ratingAverage: Int,
        val ratingQuantity: Int,
        val saved: Boolean
    ) {
        fun toPlace(): AbstractedLandmark {
            return AbstractedLandmark(
                id = _id,
                name = name,
                image = image,
                location = govName,
                isSaved = saved,
                rating = ratingAverage.toFloat(),
                ratingCount = ratingQuantity
            )
        }
    }

    fun toDomainHome(): HomeResponse {
        return HomeResponse(
            event = event.map { it.toDomainEvent() },
            explore = explore.map { it.toPlace() },
            recentlyAdded = recentlyAdded.map { it.toPlace() },
            recentlyViewed = recentlyViewed.map { it.toPlace() },
            suggestedForYou = suggestedForYou.map { it.toPlace() },
            topRated = topRated.map { it.toPlace() }
        )
    }
}