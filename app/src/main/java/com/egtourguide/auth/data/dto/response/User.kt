package com.egtourguide.auth.data.dto.response

data class User(
    val username: String,
    val phone: String,
    val role: String,
    val email: String,
    val active: Boolean,
    val _id: String,
    val __v: Int
)