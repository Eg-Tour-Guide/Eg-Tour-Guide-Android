package com.egtourguide.auth.domain.usecases

import com.egtourguide.auth.data.dto.body.ForgotPasswordRequestBody
import com.egtourguide.auth.domain.repository.AuthRepository
import javax.inject.Inject

class GetForgotPasswordCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String) =
        authRepository.getForgotPasswordCode(
            requestBody = ForgotPasswordRequestBody(email = email)
        )
}