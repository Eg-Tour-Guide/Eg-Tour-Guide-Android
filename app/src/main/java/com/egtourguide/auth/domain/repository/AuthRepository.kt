package com.egtourguide.auth.domain.repository

import com.egtourguide.auth.data.dto.body.ForgotPasswordRequestBody
import com.egtourguide.auth.data.dto.body.ResetPasswordRequestBody
import com.egtourguide.auth.domain.model.ForgotPasswordResponse
import com.egtourguide.core.utils.ResultWrapper
import kotlinx.coroutines.flow.Flow

interface AuthRepository {

    suspend fun getForgotPasswordCode(
        requestBody: ForgotPasswordRequestBody
    ): Flow<ResultWrapper<ForgotPasswordResponse>>

    suspend fun resetPassword(
        code: String,
        requestBody: ResetPasswordRequestBody
    ): Flow<ResultWrapper<Unit>>
}