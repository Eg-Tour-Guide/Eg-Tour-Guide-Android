package com.egtourguide.auth.domain.usecases

import com.egtourguide.auth.data.dto.body.SendCodeRequestBody
import com.egtourguide.auth.domain.repository.AuthRepository
import javax.inject.Inject

class SendCodeUseCase @Inject constructor(private val repository: AuthRepository) {

    suspend operator fun invoke(email: String) = repository.sendCode(
        SendCodeRequestBody(email = email)
    )
}