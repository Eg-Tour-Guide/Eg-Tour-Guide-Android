package com.egtourguide.auth.data.dto.response

data class Data(
    val token: String,
    val user: User
)

data class User(
    val username: String,
    val phone: String,
    val email: String,
    val _id: String,
    val __v: Int
)