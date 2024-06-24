package com.egtourguide.auth.data.repository

import com.egtourguide.auth.data.AuthApi
import com.egtourguide.auth.data.dto.body.ForgotPasswordRequestBody
import com.egtourguide.auth.data.dto.body.LoginRequestBody
import com.egtourguide.auth.data.dto.body.ResetPasswordRequestBody
import com.egtourguide.auth.data.dto.body.SendCodeRequestBody
import com.egtourguide.auth.data.dto.body.SignupRequestBody
import com.egtourguide.auth.domain.repository.AuthRepository
import com.egtourguide.core.utils.safeCall
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(private val authApi: AuthApi) : AuthRepository {

    override suspend fun getForgotPasswordCode(
        requestBody: ForgotPasswordRequestBody
    ) = safeCall {
        authApi.getForgotPasswordCode(requestBody = requestBody).toForgotPasswordResponse()
    }

    override suspend fun resetPassword(
        code: String,
        requestBody: ResetPasswordRequestBody
    ) = safeCall {
        authApi.resetPassword(requestBody = requestBody, code = code)
    }

    override suspend fun login(
        requestBody: LoginRequestBody
    ) = safeCall {
        authApi.login(requestBody = requestBody).toLoginResponse()
    }

    override suspend fun sendCode(requestBody: SendCodeRequestBody) = safeCall {
        authApi.sendCode(requestBody = requestBody).toSendCodeResponse()
    }

    override suspend fun signup(requestBody: SignupRequestBody) = safeCall {
        authApi.signup(requestBody = requestBody).toSignupResponse()
    }
}