package com.egtourguide.expanded.data.repository

import com.egtourguide.core.utils.safeCall
import com.egtourguide.expanded.data.ExpandedApi
import com.egtourguide.expanded.domain.repository.ExpandedRepository
import com.egtourguide.expanded.data.dto.body.AddPlaceBody
import com.egtourguide.expanded.data.dto.body.ReviewRequestBody
import javax.inject.Inject

class ExpandedRepositoryImpl @Inject constructor(
    private val expandedApi: ExpandedApi
) : ExpandedRepository {

    override suspend fun getLandmark(id: String) = safeCall {
        expandedApi.getLandmark(landmarkID = id).toLandmark()
    }

    override suspend fun getArtifact(id: String) = safeCall {
        expandedApi.getArtifact(artifactID = id).toArtifact()
    }

    override suspend fun getTour(tourId: String) = safeCall {
        expandedApi.getTour(tourId = tourId).toTour()
    }

    override suspend fun getEvent(eventId: String) = safeCall {
        expandedApi.getEvent(eventId).toSingleEvent()
    }

    override suspend fun addPlaceToTour(
        tourId: String,
        addPlaceBody: AddPlaceBody
    ) = safeCall {
        expandedApi.addPlaceToTour(tourId = tourId, place = addPlaceBody)
    }

    override suspend fun sendTourReview(
        tourId: String,
        requestBody: ReviewRequestBody
    ) = safeCall {
        expandedApi.reviewTour(tourId, requestBody)
    }


    override suspend fun sendPlaceReview(
        placeId: String,
        requestBody: ReviewRequestBody
    ) = safeCall {
        expandedApi.reviewPlace(placeId, requestBody)
    }

    override suspend fun getTourDetails(tourId: String) = safeCall {
        expandedApi.getTourDetails(tourId).toTourDetails()
    }
}