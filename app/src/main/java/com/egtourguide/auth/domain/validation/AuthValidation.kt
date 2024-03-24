package com.egtourguide.auth.domain.validation

object AuthValidation {

    private val emailRegex = Regex(pattern = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")

    fun validatePassword(password: String): Boolean {
        return password.length >= 8
    }

    fun validateEmail(email: String): Boolean {
        return email.matches(emailRegex)
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): Boolean {
        return password == confirmPassword
    }

}