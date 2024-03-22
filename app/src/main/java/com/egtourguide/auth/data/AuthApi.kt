package com.egtourguide.auth.data

import retrofit2.http.Body
import retrofit2.http.POST
import com.egtourguide.auth.data.dto.body.ForgotPasswordRequestBody
import com.egtourguide.auth.data.dto.response.ForgotPasswordResponseDTO
import com.egtourguide.core.utils.ResultWrapper

interface AuthApi {

    @POST("auth/forget-password")
    suspend fun getForgotPasswordCode(
        @Body requestBody: ForgotPasswordRequestBody
    ): ForgotPasswordResponseDTO
}