package com.egtourguide.auth.domain.usecases

import com.egtourguide.auth.data.dto.body.LoginRequestBody
import com.egtourguide.auth.domain.repository.AuthRepository
import javax.inject.Inject

class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String) =
        authRepository.login(
            requestBody = LoginRequestBody(
                email = email,
                password = password
            )
        )
}