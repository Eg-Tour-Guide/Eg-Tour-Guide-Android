package com.egtourguide.auth.domain.usecases

import com.egtourguide.auth.data.dto.body.SignupRequestBody
import com.egtourguide.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SignupUseCase @Inject constructor(private val repository: AuthRepository) {

    // TODO: Change the function!!
    suspend operator fun invoke(
        name: String,
        email: String,
        phone: String,
        password: String,
        confirmPassword: String
    ) = repository.signup(
        requestBody = SignupRequestBody(
            firstName = "test",
            lastName = "test",
            password = password,
            email = email,
            gender = "male",
            governmentLocation = "damietta",
            username = name,
            phone = phone
        )
    )
}