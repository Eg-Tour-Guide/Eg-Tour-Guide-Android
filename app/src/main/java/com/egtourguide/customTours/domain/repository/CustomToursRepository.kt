package com.egtourguide.customTours.domain.repository

import com.egtourguide.core.utils.ResultWrapper
import com.egtourguide.customTours.data.dto.body.CreateTourBody
import com.egtourguide.customTours.data.dto.body.EditTourBody
import com.egtourguide.home.domain.model.AbstractedTour
import kotlinx.coroutines.flow.Flow

interface CustomToursRepository {

    suspend fun getMyTours(): Flow<ResultWrapper<List<AbstractedTour>>>

    suspend fun createTour(body: CreateTourBody): Flow<ResultWrapper<Unit>>

    suspend fun editTour(tourId: String, body: EditTourBody): Flow<ResultWrapper<Unit>>

    suspend fun removePlaceFromTour(
        tourId: String,
        placeId: String
    ): Flow<ResultWrapper<Unit>>
}