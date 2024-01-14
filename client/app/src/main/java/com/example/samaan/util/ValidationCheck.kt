package com.example.samaan.util

import android.util.Patterns

fun validateEmail(email: String): RegisterValidation {
    return if (email.isEmpty()) {
        RegisterValidation.Failed("Email cannot be empty")
    } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
        RegisterValidation.Failed("Email is invalid")
    } else {
        RegisterValidation.Success
    }
}

fun validatePassword(password: String): RegisterValidation {
    return if (password.isEmpty()) {
        RegisterValidation.Failed("Password cannot be empty")
    } else if (password.length < 8) {
        RegisterValidation.Failed("Password must be at least 8 characters")
    } else {
        RegisterValidation.Success
    }
}