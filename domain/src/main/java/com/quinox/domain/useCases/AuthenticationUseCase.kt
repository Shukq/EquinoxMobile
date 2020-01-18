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
    fun signUp(username: String, password: String, name: String, gender: Gender, birthDate: String, occupation: String) : Observable<Result<SignUpResult>>
    fun confirmSignUp(username: String,confirmationCode : String) : Observable<Result<Boolean>>
    fun resendConfirmationCode(username: String) : Observable<Result<Boolean>>

    fun getAttributes() : Observable<List<Pair<String,String>>>
    fun initForgotPassword(email : String) : Observable<Result<String>>
    fun confirmForgotPassword(newPassword: String, code: String) : Observable<Result<Boolean>>
}