package com.egtourguide.auth.presentation.screens.login

data class LoginState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val isError: Boolean = false,
    val isSuccess: Boolean = false,
    val error: String? = null,
    val emailError: Boolean = false,
    val passwordError: Boolean=false
)
