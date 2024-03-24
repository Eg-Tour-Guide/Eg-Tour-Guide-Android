package com.egtourguide.auth.domain.model


data class LoginResponse(
    val token: String,
    val status: String
)