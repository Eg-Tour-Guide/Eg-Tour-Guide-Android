package com.egtourguide.auth.presentation.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String = "",
    val emailError: String? = null,
    val passwordError: String?=null
)
