package com.egtourguide.auth.data.dto.response

import com.egtourguide.auth.domain.model.LoginResponse

data class LoginResponseDTO(
    val `data`: Data,
    val status: String
) {
    data class Data(
        val token: String,
        val user: User
    ) {
        data class User(
            val __v: Int,
            val _id: String,
            val email: String,
            val phone: String,
            val username: String
        )
    }

    fun toLoginResponse(): LoginResponse {
        return LoginResponse(
            token = data.token,
            status = status

        )
    }
}