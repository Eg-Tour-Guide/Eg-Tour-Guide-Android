package com.egtourguide.auth.data.dto.response

import com.egtourguide.auth.domain.model.SignupResponse

data class SignupResponseDTO(
    val status: String,
    val `data`: Data
) {
    fun toSignupResponse() = SignupResponse(
        token = data.token,
        userName = data.user.username,
        email = data.user.email,
        phone = data.user.phone
    )
}