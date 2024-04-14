package com.egtourguide.auth.data.dto.body

// TODO: Change this body!!
data class SignupRequestBody(
    val firstName: String,
    val lastName: String,
    val password: String,
    val email: String,
    val gender: String,
    val governmentLocation: String,
    val username: String,
    val phone: String
)
