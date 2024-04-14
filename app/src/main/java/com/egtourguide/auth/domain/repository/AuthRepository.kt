package com.egtourguide.auth.domain.repository

import com.egtourguide.auth.data.dto.body.ForgotPasswordRequestBody
import com.egtourguide.auth.data.dto.body.LoginRequestBody
import com.egtourguide.auth.data.dto.body.ResetPasswordRequestBody
import com.egtourguide.auth.data.dto.body.SendCodeRequestBody
import com.egtourguide.auth.data.dto.body.SignupRequestBody
import com.egtourguide.auth.domain.model.ForgotPasswordResponse
import com.egtourguide.auth.domain.model.LoginResponse
import com.egtourguide.auth.domain.model.SignupResponse
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

    suspend fun login(
        requestBody: LoginRequestBody
    ): Flow<ResultWrapper<LoginResponse>>

    // TODO: Change return type!!
    suspend fun sendCode(requestBody: SendCodeRequestBody): Flow<ResultWrapper<String>>

    suspend fun signup(requestBody: SignupRequestBody): Flow<ResultWrapper<SignupResponse>>
}