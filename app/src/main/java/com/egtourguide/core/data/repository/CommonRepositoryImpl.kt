package com.egtourguide.core.data.repository

import com.egtourguide.core.data.CommonApi
import com.egtourguide.core.domain.repository.CommonRepository
import com.egtourguide.core.utils.safeCall
import javax.inject.Inject

class CommonRepositoryImpl @Inject constructor(
    private val commonApi: CommonApi
) : CommonRepository {

    override suspend fun changeLandmarkSavedState(placeId: String) = safeCall {
        commonApi.changePlaceSavedState(placeId)
    }

    override suspend fun changeArtifactSavedState(artifactId: String) = safeCall {
        commonApi.changeArtifactSavedState(artifactId)
    }

    override suspend fun changeTourSavedState(tourId: String) = safeCall {
        commonApi.changeTourSavedState(tourId)
    }
}