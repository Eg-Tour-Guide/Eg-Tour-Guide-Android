package com.egtourguide.home.data.repository

import com.egtourguide.core.utils.ResultWrapper
import com.egtourguide.core.utils.safeCall
import com.egtourguide.home.data.HomeApi
import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.domain.model.SearchResult
import com.egtourguide.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeApi: HomeApi) : HomeRepository {

    // TODO: Change these
    override suspend fun getLandmark(id: String) = safeCall {
        homeApi.getLandmark()
    }

    override suspend fun getArtifact(id: String) = safeCall {
        homeApi.getArtifact()
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

    override suspend fun deleteSearchHistory() = safeCall {
        homeApi.deleteSearchHistory()
    }
}