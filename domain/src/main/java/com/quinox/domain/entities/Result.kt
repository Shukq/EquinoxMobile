package com.quinox.domain.entities

sealed class Result<out T : Any> {
    data class success<out T : Any>(val value: T? = null) : Result<T>()
    data class failure(val cause: Exception? = null) : Result<Nothing>()

    fun isFail(): Boolean = when(this){
        is success -> false
        is failure -> true
    }

    fun successValue(): T? = when(this){
        is success -> value
        is failure -> null
    }
}