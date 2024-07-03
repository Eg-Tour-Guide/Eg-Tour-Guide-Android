package com.egtourguide.expanded.domain.repository

import com.egtourguide.core.utils.ResultWrapper
import com.egtourguide.expanded.data.dto.body.AddPlaceBody
import com.egtourguide.expanded.data.dto.body.ReviewRequestBody
import com.egtourguide.expanded.data.dto.body.TourDetailsBody
import com.egtourguide.expanded.domain.model.Artifact
import com.egtourguide.expanded.domain.model.Landmark
import com.egtourguide.expanded.domain.model.SingleEvent
import com.egtourguide.expanded.domain.model.SingleTour
import com.egtourguide.expanded.domain.model.TourDetails
import kotlinx.coroutines.flow.Flow

interface ExpandedRepository {

    suspend fun getLandmark(id: String): Flow<ResultWrapper<Landmark>>

    suspend fun getArtifact(id: String): Flow<ResultWrapper<Artifact>>

    suspend fun getTour(tourId: String): Flow<ResultWrapper<SingleTour>>

    suspend fun getEvent(eventId: String): Flow<ResultWrapper<SingleEvent>>

    suspend fun addPlaceToTour(
        tourId: String,
        addPlaceBody: AddPlaceBody
    ): Flow<ResultWrapper<Unit>>

    suspend fun sendTourReview(
        tourId: String,
        requestBody: ReviewRequestBody
    ): Flow<ResultWrapper<Unit>>

    suspend fun sendPlaceReview(
        placeId: String,
        requestBody: ReviewRequestBody
    ): Flow<ResultWrapper<Unit>>

    suspend fun getTourDetails(tourId: String): Flow<ResultWrapper<TourDetails>>

    suspend fun updateTourDetails(
        tourId: String,
        tourDetails: TourDetailsBody
    ): Flow<ResultWrapper<String>> // TODO: Change This!!
}