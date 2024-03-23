package com.egtourguide.auth.domain.use_cases

import com.egtourguide.auth.data.dto.body.ForgotPasswordRequestBody
import com.egtourguide.auth.domain.repository.AuthRepository
import javax.inject.Inject

class GetForgotPasswordCodeUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(requestBody: ForgotPasswordRequestBody) =
        authRepository.getForgotPasswordCode(requestBody = requestBody)

}