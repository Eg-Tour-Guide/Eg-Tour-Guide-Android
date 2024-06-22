package com.egtourguide.auth.data

import retrofit2.http.Body
import retrofit2.http.POST
import com.egtourguide.auth.data.dto.body.ForgotPasswordRequestBody
import com.egtourguide.auth.data.dto.body.LoginRequestBody
import com.egtourguide.auth.data.dto.body.ResetPasswordRequestBody
import com.egtourguide.auth.data.dto.body.SendCodeRequestBody
import com.egtourguide.auth.data.dto.body.SignupRequestBody
import com.egtourguide.auth.data.dto.response.ForgotPasswordResponseDTO
import com.egtourguide.auth.data.dto.response.LoginResponseDTO
import com.egtourguide.auth.data.dto.response.SignupResponseDTO
import retrofit2.http.PATCH
import retrofit2.http.Path

interface AuthApi {

    @POST("api/v1/auth/forget-password")
    suspend fun getForgotPasswordCode(
        @Body requestBody: ForgotPasswordRequestBody
    ): ForgotPasswordResponseDTO

    @PATCH("api/v1/auth/reset-password/{code}")
    suspend fun resetPassword(
        @Body requestBody: ResetPasswordRequestBody,
        @Path("code") code: String
    )

    @POST("api/v1/auth/login")
    suspend fun login(
        @Body requestBody: LoginRequestBody
    ): LoginResponseDTO

    // TODO: Change Response Model!
    @POST("auth/send-code")
    suspend fun sendCode(
        @Body requestBody: SendCodeRequestBody
    ): String

    @POST("auth/signup")
    suspend fun signup(
        @Body requestBody: SignupRequestBody
    ): SignupResponseDTO
}