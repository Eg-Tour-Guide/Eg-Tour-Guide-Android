package com.egtourguide.auth.domain.usecases

import com.egtourguide.auth.data.dto.body.ResetPasswordRequestBody
import com.egtourguide.auth.domain.repository.AuthRepository
import javax.inject.Inject

class ResetPasswordUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(code: String, requestBody: ResetPasswordRequestBody) =
        authRepository.resetPassword(code = code, requestBody = requestBody)
}