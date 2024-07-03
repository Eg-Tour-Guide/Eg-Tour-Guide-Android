package com.egtourguide.customTours.domain.repository

import com.egtourguide.core.utils.ResultWrapper
import com.egtourguide.home.domain.model.AbstractedTour
import kotlinx.coroutines.flow.Flow

interface CustomToursRepository {

    suspend fun getMyTours(): Flow<ResultWrapper<List<AbstractedTour>>>
}