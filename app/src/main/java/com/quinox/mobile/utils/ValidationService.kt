package com.quinox.mobile.utils

import android.util.Log
import android.util.Patterns
import java.util.regex.Pattern

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

    private fun isValidName(username: String): Boolean{
        return username.isNotEmpty()
    }

    private fun isValidOcupation(profession: String): Boolean{
        return profession.isNotEmpty()
    }

    private fun isValidDate(date: String?): Boolean{
        return !date.isNullOrEmpty()
    }

    fun validateUserRegister(username: String, email: String, profession: String, date: String?): Boolean{
        return isValidName(username) && isValidEmail(email) && isValidOcupation(profession) && isValidDate(date)
    }

    fun validatePasswordLenght(password : String) : Boolean{
        val minPasswordLength = 8
        val maxPasswordLength = 20
        if(password.length < minPasswordLenght || password.length > maxPasswordLenght){
            return false
        }
        return true
    }

    fun validatePasswordCapitalLowerLetters(password: String) : Boolean {
        val pattern1 = Pattern.compile(".[A-Z].")
        val pattern1Sub = Pattern.compile(".[a-z].")
        if (pattern1.matcher(password).matches() && pattern1Sub.matcher(password).matches()){
            return true
        }
        return false
    }

    fun validatePasswordNumbers(password: String): Boolean{
        val pattern2 = Pattern.compile(".\\d.")
        if(pattern2.matcher(password).matches()){
            return true
        }
        return false
    }

    fun validatePasswordSpecialCharacters(password: String) : Boolean {
        val pattern3 = Pattern.compile(".[!$#@_.+-].")
        if(pattern3.matcher(password).matches()){
            return true
        }
        return false
    }

    fun validatePassword(password: String) : Boolean {
        var isSuccess = true
        if (!validatePasswordLenght(password)){isSuccess = false}
        if (!validatePasswordCapitalLowerLetters(password)){isSuccess = false}
        if (!validatePasswordNumbers(password)){isSuccess = false}
        if (!validatePasswordSpecialCharacters(password)){isSuccess = false}

        return isSuccess
    }

}