package com.egtourguide.home.data.repository

import com.egtourguide.core.utils.safeCall
import com.egtourguide.home.data.HomeApi
import com.egtourguide.home.domain.repository.HomeRepository
import okhttp3.MultipartBody
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeApi: HomeApi) : HomeRepository {

    override suspend fun getHome() = safeCall {
        homeApi.getHome().toDomainHome()
    }

    override suspend fun getLandmarksList() = safeCall {
        homeApi.getLandmarksList().places.map { it.toDomainPlace() }
    }

    override suspend fun getArtifactsList() = safeCall {
        homeApi.getArtifactsList().artifacs.map { it.toDomainAbstractedArtifact() }
    }

    override suspend fun getToursList() = safeCall {
        homeApi.getToursList().toDomainToursList()
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

    override suspend fun detectArtifact(image: MultipartBody.Part) = safeCall {
        homeApi.detectArtifact(photo = image).toDomainDetectedArtifact()
    }
}