package com.quinox.mobile.mocks

import com.quinox.domain.entities.*
import com.quinox.domain.useCases.AuthenticationUseCase
import io.reactivex.Observable

class AuthenticationUseCase : AuthenticationUseCase {
    override fun resendConfirmationCode(username: String): Observable<Result<Boolean>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun confirmSignUp(
        username: String,
        confirmationCode: String
    ): Observable<Result<Boolean>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCurrentUserState(): Observable<UserStateResult> {
        return Observable.create create@{ observer ->
            val response = UserStateResult.signedOut
            observer.onNext(response)
            observer.onComplete()
        }
    }

    override fun signIn(username: String, password: String): Observable<Result<SignInResult>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signOut(): Observable<Result<UserStateResult>> {
        return Observable.create<Result<UserStateResult>> create@{ observer ->
            val signOutResult = UserStateResult.signedOut
            val result = Result.success(signOutResult)
            observer.onNext(result)
            observer.onComplete()
        }
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