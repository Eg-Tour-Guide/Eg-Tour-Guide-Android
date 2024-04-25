package com.egtourguide.home.domain.repository

import com.egtourguide.core.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface HomeRepository {

    // TODO: Change these!!
    suspend fun getLandmark(id: String): Flow<ResultWrapper<String>>
    suspend fun getArtifact(id: String): Flow<ResultWrapper<String>>
}