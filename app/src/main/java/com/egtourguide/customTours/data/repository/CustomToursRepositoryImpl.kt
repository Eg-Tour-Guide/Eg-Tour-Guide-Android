package com.egtourguide.customTours.data.repository

import com.egtourguide.core.utils.safeCall
import com.egtourguide.customTours.data.CustomToursApi
import com.egtourguide.customTours.data.dto.body.CreateTourBody
import com.egtourguide.customTours.data.dto.body.EditTourBody
import com.egtourguide.customTours.domain.repository.CustomToursRepository
import javax.inject.Inject

class CustomToursRepositoryImpl @Inject constructor(
    private val customToursApi: CustomToursApi
) : CustomToursRepository {

    override suspend fun getMyTours() = safeCall {
        customToursApi.getMyTours().tours.map { it.toDomainAbstractedTour() }
    }

    override suspend fun createTour(body: CreateTourBody) = safeCall {
        customToursApi.createTour(body)
    }

    override suspend fun editTour(tourId: String, body: EditTourBody) = safeCall {
        customToursApi.editTour(tourId = tourId, body = body)
    }

    override suspend fun removePlaceFromTour(
        tourId: String,
        placeId: String
    ) = safeCall {
        customToursApi.removePlaceFromTour(tourId = tourId, placeId = placeId)
    }
}