package com.egtourguide.user.domain.repository

import com.egtourguide.core.utils.ResultWrapper
import com.egtourguide.user.domain.model.SavedItem
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getSavedList(): Flow<ResultWrapper<List<SavedItem>>>
}