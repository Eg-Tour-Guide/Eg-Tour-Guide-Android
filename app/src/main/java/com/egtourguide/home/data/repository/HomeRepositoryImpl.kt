package com.egtourguide.home.data.repository

import com.egtourguide.core.utils.safeCall
import com.egtourguide.home.data.HomeApi
import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeApi: HomeApi) : HomeRepository {

    override suspend fun getLandmark(id: String) = safeCall {
        homeApi.getLandmark(landmarkID = id).toLandmark()
    }

    override suspend fun getArtifact(id: String) = safeCall {
        homeApi.getArtifact(artifactID = id).toArtifact()
    }

    override suspend fun getHome() = safeCall {
        homeApi.getHome().toDomainHome()
    }

    override suspend fun changePlaceSavedState(placeId: String) = safeCall {
        homeApi.changePlaceSavedState(placeId)
    }

    override suspend fun getLandmarksList() = safeCall {
        homeApi.getLandmarksList().places.map { it.toDomainPlace() }
    }

    override suspend fun getArtifactsList() = safeCall {
        homeApi.getArtifactsList().artifacs.map { it.toDomainAbstractedArtifact() }
    }

    override suspend fun changeArtifactSavedState(artifactId: String) = safeCall {
        homeApi.changeArtifactSavedState(artifactId)
    }
}