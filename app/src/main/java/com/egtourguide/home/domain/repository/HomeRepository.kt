package com.egtourguide.home.domain.repository

import com.egtourguide.core.utils.ResultWrapper
import com.egtourguide.home.domain.model.AbstractedArtifact
import com.egtourguide.home.domain.model.AbstractedTour
import com.egtourguide.home.domain.model.Home
import com.egtourguide.home.domain.model.Place
import com.egtourguide.home.domain.model.SearchResult
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    // TODO: Change these!!
    suspend fun getLandmark(id: String): Flow<ResultWrapper<String>>
    suspend fun getArtifact(id: String): Flow<ResultWrapper<String>>
    suspend fun getHome(): Flow<ResultWrapper<Home>>
    suspend fun changePlaceSavedState(placeId: String): Flow<ResultWrapper<Unit>>
    suspend fun getLandmarksList(): Flow<ResultWrapper<List<Place>>>
    suspend fun getArtifactsList(): Flow<ResultWrapper<List<AbstractedArtifact>>>
    suspend fun changeArtifactSavedState(artifactId: String): Flow<ResultWrapper<Unit>>
    suspend fun getToursList(): Flow<ResultWrapper<List<AbstractedTour>>>
    suspend fun changeTourSavedState(tourId: String): Flow<ResultWrapper<Unit>>
    suspend fun search(query: String): Flow<ResultWrapper<List<SearchResult>>>
    suspend fun getSearchHistory(): Flow<ResultWrapper<List<String>>>
}