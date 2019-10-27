package com.quinox.domain.entities

import java.io.Serializable

enum class SignUpState {
    confirmed, unconfirmed, unknown
}
class SignUpResult(var state: SignUpState, var username: String, var password: String) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (other == null || other !is SignUpResult)
        {
            return false
        }
        else
        {
            return other.username == username && other.password == password
        }
    }

    override fun hashCode(): Int {
        var result = state.hashCode()
        result = 31 * result + username.hashCode()
        result = 31 * result + password.hashCode()
        return result
    }
}
