package com.egtourguide.auth.data.dto.body

data class SignupRequestBody(
    val password: String,
    val email: String,
    val username: String,
    val phone: String
)
