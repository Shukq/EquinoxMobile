package com.quinox.domain.entities

import java.io.Serializable

class SignUpModel(
    val name: String,
    val email: String,
    val ocupation: String,
    val date: String,
    val gender: Gender
): Serializable