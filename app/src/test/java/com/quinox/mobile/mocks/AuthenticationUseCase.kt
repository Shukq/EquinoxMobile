package com.quinox.mobile.mocks

import com.quinox.domain.entities.*
import com.quinox.domain.useCases.AuthenticationUseCase
import io.reactivex.Observable

class AuthenticationUseCase : AuthenticationUseCase {
    override fun resendConfirmationCode(username: String): Observable<Result<Boolean>> {
        return Observable.create<Result<Boolean>> create@{ observer ->
            if (username == "loktar@gmail.com"){
                observer.onNext(Result.success(true))
                observer.onComplete()
            }
            else{
                observer.onNext(Result.failure(Exception()))
                observer.onComplete()
            }
        }
    }

    override fun confirmSignUp(
        username: String,
        confirmationCode: String
    ): Observable<Result<Boolean>> {
        return Observable.create<Result<Boolean>> create@{ observer ->
            if (username == "loktar@gmail.com" && confirmationCode == "123456"){
                observer.onNext(Result.success(true))
                observer.onComplete()
            }
            else{
                observer.onNext(Result.failure(Exception()))
                observer.onComplete()
            }
        }
    }

    override fun getCurrentUserState(): Observable<UserStateResult> {
        return Observable.create create@{ observer ->
            val response = UserStateResult.signedOut
            observer.onNext(response)
            observer.onComplete()
        }
    }

    override fun signIn(username: String, password: String): Observable<Result<SignInResult>> {
        return Observable.create<Result<SignInResult>> create@{ observer ->
            if (username == "loktar@gmail.com" && password == "Memes1234_"){
                val model = SignInResult("Ok")
                observer.onNext(Result.success(model))
                observer.onComplete()
            }
            else{
                observer.onNext(Result.failure(Exception()))
                observer.onComplete()
            }
        }
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
        return Observable.create<Result<SignUpResult>> create@{ observer ->
            if (username == "loktar@gmail.com" && password == "Memes1234_"){
                val model = SignUpResult(SignUpState.confirmed,username,password)
                observer.onNext(Result.success(model))
                observer.onComplete()
            }
            else{
                observer.onNext(Result.failure(Exception()))
                observer.onComplete()
            }
        }
    }
}