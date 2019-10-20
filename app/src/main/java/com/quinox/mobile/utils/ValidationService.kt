package com.quinox.mobile.utils

import android.util.Patterns

object ValidationService {

    private val minPasswordLenght = 8
    private val maxPasswordLenght = 20

    fun isValidEmail(username : String): Boolean{
        return Patterns.EMAIL_ADDRESS.matcher(username).matches()
    }

    fun validateUserCredentials(username: String, password : String) : Boolean{
        var isValid = false
        if( password.length < minPasswordLenght || password.length> maxPasswordLenght) {
            return false
        }

        if (isValidEmail(username)) {
            isValid = true
        }

        return isValid
    }
}