package com.egtourguide.core.domain.repository

import com.egtourguide.core.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface CommonRepository {

    suspend fun changeLandmarkSavedState(placeId: String): Flow<ResultWrapper<Unit>>

    suspend fun changeArtifactSavedState(artifactId: String): Flow<ResultWrapper<Unit>>

    suspend fun changeTourSavedState(tourId: String): Flow<ResultWrapper<Unit>>
}