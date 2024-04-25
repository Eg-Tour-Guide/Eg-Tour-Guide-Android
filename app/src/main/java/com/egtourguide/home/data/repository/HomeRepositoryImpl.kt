package com.egtourguide.home.data.repository

import com.egtourguide.core.utils.safeCall
import com.egtourguide.home.data.HomeApi
import com.egtourguide.home.domain.repository.HomeRepository
import javax.inject.Inject

class HomeRepositoryImpl @Inject constructor(private val homeApi: HomeApi) : HomeRepository {

    // TODO: Change these
    override suspend fun getLandmark(id: String) = safeCall {
        homeApi.getLandmark()
    }

    override suspend fun getArtifact(id: String) = safeCall {
        homeApi.getArtifact()
    }
}