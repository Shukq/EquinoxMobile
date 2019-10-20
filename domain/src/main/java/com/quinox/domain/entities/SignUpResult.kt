package com.quinox.domain.entities

enum class SignUpState {
    confirmed, unconfirmed, unknown
}
class SignUpResult(var state: SignUpState, var username: String, var password: String) {
}
