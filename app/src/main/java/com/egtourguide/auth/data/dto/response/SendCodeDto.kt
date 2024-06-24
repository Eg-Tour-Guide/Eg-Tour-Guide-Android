package com.egtourguide.auth.data.dto.response

import com.egtourguide.auth.domain.model.SendCodeResponse

data class SendCodeDto(
    val status: String,
    val message: String,
    val code: String,
    val email: String
) {
    fun toSendCodeResponse() = SendCodeResponse(
        code = code
    )
}