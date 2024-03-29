package com.egtourguide.auth.data

import retrofit2.http.Body
import retrofit2.http.POST
import com.egtourguide.auth.data.dto.body.ForgotPasswordRequestBody
import com.egtourguide.auth.data.dto.body.LoginRequestBody
import com.egtourguide.auth.data.dto.body.ResetPasswordRequestBody
import com.egtourguide.auth.data.dto.response.ForgotPasswordResponseDTO
import com.egtourguide.auth.data.dto.response.LoginResponseDTO
import com.egtourguide.core.utils.ResultWrapper
import retrofit2.http.Path

interface AuthApi {

    @POST("auth/forget-password")
    suspend fun getForgotPasswordCode(
        @Body requestBody: ForgotPasswordRequestBody
    ): ForgotPasswordResponseDTO

    @POST("auth/reset-password/{code}")
    suspend fun resetPassword(
        @Body requestBody: ResetPasswordRequestBody,
        @Path("code") code: String
    )

    @POST("auth/login")
    suspend fun login(
        @Body requestBody: LoginRequestBody
    ): LoginResponseDTO
}