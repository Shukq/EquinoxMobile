package com.quinox.mobile.extensions

import io.reactivex.Observable
import com.quinox.domain.entities.Result

fun <T: Any> Observable<Result<T>>.resultValues() : Observable<T>{
    return this.filter { !it.isFail() }
        .map { it.successValue() }
}

fun <T: Any> Observable<Result<T>>.resultErrors() : Observable<Exception> {
    return this.filter { it.isFail() }
        .map {
            when (it) {
                is Result.failure -> return@map it.cause
                else -> return@map null
            }
        }
}