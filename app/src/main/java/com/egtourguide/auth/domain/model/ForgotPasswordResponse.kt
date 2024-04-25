package com.egtourguide.auth.domain.model

data class ForgotPasswordResponse(
    val message: String,
    val code: String
)
