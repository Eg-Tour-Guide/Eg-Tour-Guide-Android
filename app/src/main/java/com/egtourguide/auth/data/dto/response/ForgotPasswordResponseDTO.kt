package com.egtourguide.auth.data.dto.response

import com.egtourguide.auth.domain.model.ForgotPasswordResponse

data class ForgotPasswordResponseDTO(
    val message: String,
    val status: String,
    val code: String
) {
    fun toForgotPasswordResponse() = ForgotPasswordResponse(message = message, code = code)
}