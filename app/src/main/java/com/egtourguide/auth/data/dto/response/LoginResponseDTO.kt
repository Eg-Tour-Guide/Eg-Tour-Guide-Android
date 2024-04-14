package com.egtourguide.auth.data.dto.response

import com.egtourguide.auth.domain.model.LoginResponse

data class LoginResponseDTO(
    val `data`: Data,
    val status: String
) {
    fun toLoginResponse() = LoginResponse(
        token = data.token
    )
}