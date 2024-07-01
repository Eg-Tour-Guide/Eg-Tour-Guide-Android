package com.egtourguide.auth.domain.validation

enum class ValidationCases {
    CORRECT, EMPTY, ERROR, NOT_MATCHED
}

object AuthValidation {

    private val emailRegex = Regex(pattern = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}\$")
    private val passwordRegex =
        Regex(pattern = "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@\$%^&*-]).{8,}\$")

    fun validateName(name: String): ValidationCases {
        return if (name.isEmpty()) ValidationCases.EMPTY
        else if (name.length < 3) ValidationCases.ERROR
        else ValidationCases.CORRECT
    }

    fun validatePhone(phone: String): ValidationCases {
        return if (phone.isEmpty()) ValidationCases.EMPTY
        else if (phone.length < 8) ValidationCases.ERROR
        else ValidationCases.CORRECT
    }

    fun validateCode(sentCode: String, code: String): ValidationCases {
        return if (code.isEmpty()) ValidationCases.EMPTY
        else if (code != sentCode) ValidationCases.NOT_MATCHED
        else ValidationCases.CORRECT
    }

    fun validatePassword(password: String): ValidationCases {
        return if (password.isEmpty()) ValidationCases.EMPTY
        else if (password.length < 8 || !password.matches(passwordRegex)) ValidationCases.ERROR
        else ValidationCases.CORRECT
    }

    fun validateEmail(email: String): ValidationCases {
        return if (email.isEmpty()) ValidationCases.EMPTY
        else if (!email.matches(emailRegex)) ValidationCases.ERROR
        else ValidationCases.CORRECT
    }

    fun validateConfirmPassword(password: String, confirmPassword: String): ValidationCases {
        return if (confirmPassword.isEmpty()) ValidationCases.EMPTY
        else if (password != confirmPassword) ValidationCases.NOT_MATCHED
        else ValidationCases.CORRECT
    }
}