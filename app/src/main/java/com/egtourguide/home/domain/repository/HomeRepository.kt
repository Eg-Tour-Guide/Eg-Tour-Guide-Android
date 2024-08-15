package com.egtourguide.home.domain.repository

import com.egtourguide.core.utils.ResultWrapper
import com.egtourguide.home.domain.model.ArtifactsScreenResponse
import com.egtourguide.home.domain.model.DetectedArtifact
import com.egtourguide.home.domain.model.HomeResponse
import com.egtourguide.home.domain.model.LandmarksScreenResponse
import com.egtourguide.home.domain.model.SearchResult
import com.egtourguide.home.domain.model.ToursScreenResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.MultipartBody

interface HomeRepository {

    suspend fun getHome(): Flow<ResultWrapper<HomeResponse>>

    suspend fun getLandmarksList(): Flow<ResultWrapper<LandmarksScreenResponse>>

    suspend fun getArtifactsList(): Flow<ResultWrapper<ArtifactsScreenResponse>>

    suspend fun getToursList(): Flow<ResultWrapper<ToursScreenResponse>>

    suspend fun search(query: String): Flow<ResultWrapper<List<SearchResult>>>

    suspend fun getSearchHistory(): Flow<ResultWrapper<List<String>>>

    suspend fun deleteSearchHistory(): Flow<ResultWrapper<Unit>>

    suspend fun detectArtifact(image: MultipartBody.Part): Flow<ResultWrapper<DetectedArtifact>>
}