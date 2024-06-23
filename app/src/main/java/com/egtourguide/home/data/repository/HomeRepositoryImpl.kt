package com.egtourguide.home.data.repository

import com.egtourguide.core.utils.safeCall
import com.egtourguide.home.data.HomeApi
import com.egtourguide.home.data.dto.body.TourDetailsBody
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

    override suspend fun getToursList() = safeCall {
        homeApi.getToursList().tours.map { it.toDomainAbstractedTour() }
    }

    override suspend fun changeTourSavedState(tourId: String) = safeCall {
        homeApi.changeTourSavedState(tourId)
    }

    override suspend fun search(query: String) = safeCall {
        homeApi.search(query).data.toDomainSearchResults()
    }

    override suspend fun getSearchHistory() = safeCall {
        homeApi.getSearchHistory().search.map { it.search }
    }

    override suspend fun getTourDetails(tourId: String) = safeCall {
        homeApi.getTourDetails(tourId).toTourDetails()
    }

    override suspend fun updateTourDetails(
        tourId: String,
        tourDetails: TourDetailsBody
    ) = safeCall {
        homeApi.updateTourDetails(tourId = tourId, tourDetails = tourDetails)
    }
}