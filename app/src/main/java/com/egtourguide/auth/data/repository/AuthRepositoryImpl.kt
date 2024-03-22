package com.egtourguide.auth.data.repository

import com.egtourguide.auth.data.AuthApi
import com.egtourguide.auth.data.dto.body.ForgotPasswordRequestBody
import com.egtourguide.auth.domain.repository.AuthRepository
import com.egtourguide.core.utils.safeCall
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authApi: AuthApi) : AuthRepository {
    override suspend fun getForgotPasswordCode(
        requestBody: ForgotPasswordRequestBody
    ) = safeCall {
        authApi.getForgotPasswordCode(requestBody).toForgotPasswordResponse()
    }
}