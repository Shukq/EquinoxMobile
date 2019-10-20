package com.quinox.awsplatform.useCases

import com.quinox.domain.entities.Gender
import com.quinox.domain.entities.SignInResult
import com.quinox.domain.entities.SignUpResult
import com.quinox.domain.entities.UserStateResult
import com.quinox.domain.useCases.AuthenticationUseCase
import io.reactivex.Observable

class AuthenticationUseCase : AuthenticationUseCase {
    override fun getCurrentUserState(): Observable<UserStateResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signIn(username: String, password: String): Observable<Result<SignInResult>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signOut(): Observable<Result<UserStateResult>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signUp(
        username: String,
        password: String,
        name: String,
        gender: Gender
    ): Observable<Result<SignUpResult>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}