package com.egtourguide.auth.domain.model


data class LoginResponse(
    val token: String,
    val userName: String,
    val email: String,
    val phone: String
)