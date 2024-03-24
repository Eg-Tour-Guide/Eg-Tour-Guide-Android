package com.egtourguide.auth.domain.use_cases

import com.egtourguide.auth.data.dto.body.LoginRequestBody
import com.egtourguide.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(requestBody: LoginRequestBody) =
        authRepository.login(requestBody = requestBody)
}