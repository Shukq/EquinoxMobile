package com.quinox.domain.useCases

import com.quinox.domain.entities.Gender
import com.quinox.domain.entities.SignInResult
import com.quinox.domain.entities.SignUpResult
import com.quinox.domain.entities.UserStateResult
import com.quinox.domain.entities.Result
import io.reactivex.Observable

interface AuthenticationUseCase {
    fun getCurrentUserState() : Observable<UserStateResult>
    fun signIn(username: String,password: String) : Observable<Result<SignInResult>>
    fun signOut() : Observable<Result<UserStateResult>>
    fun signUp(username: String, password: String, name: String, gender: Gender) : Observable<Result<SignUpResult>>
}