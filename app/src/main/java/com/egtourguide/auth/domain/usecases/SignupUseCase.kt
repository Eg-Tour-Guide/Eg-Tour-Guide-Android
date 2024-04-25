package com.egtourguide.auth.domain.usecases

import com.egtourguide.auth.data.dto.body.SignupRequestBody
import com.egtourguide.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignupUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(
        name: String,
        email: String,
        phone: String,
        password: String
    ) = repository.signup(
        requestBody = SignupRequestBody(
            password = password,
            email = email,
            username = name,
            phone = phone
        )
    )
}