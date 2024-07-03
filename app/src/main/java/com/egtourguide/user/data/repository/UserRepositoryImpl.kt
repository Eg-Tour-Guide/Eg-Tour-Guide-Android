package com.egtourguide.user.data.repository

import com.egtourguide.core.utils.safeCall
import com.egtourguide.user.data.UserApi
import com.egtourguide.user.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userApi: UserApi
) : UserRepository {

    override suspend fun getSavedList() = safeCall {
        userApi.getSavedItems().toDomainSavedItems()
    }
}