package com.egtourguide.auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false
)
