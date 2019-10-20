package com.quinox.awsplatform.useCases

import android.util.Log
import com.amazonaws.mobile.auth.core.SignInResultHandler
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.quinox.domain.entities.Gender
import com.quinox.domain.entities.SignInResult
import com.quinox.domain.entities.SignUpResult
import com.quinox.domain.entities.UserStateResult
import com.quinox.domain.useCases.AuthenticationUseCase
import com.quinox.domain.entities.Result
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.Exception

class AuthenticationUseCase : AuthenticationUseCase {
    override fun getCurrentUserState(): Observable<UserStateResult> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun signIn(username: String, password: String): Observable<Result<SignInResult>> {
        val observable = Single.create<Result<SignInResult>> { single ->
            AWSMobileClient.getInstance().signIn(username,password,null,object:Callback<com.amazonaws.mobile.client.results.SignInResult>{
                override fun onResult(result: com.amazonaws.mobile.client.results.SignInResult?) {
                    if (result != null) {
                        val state = result.signInState
                        val signInResult = SignInResult(state.name)
                        single.onSuccess(Result.success(signInResult))
                    }
                    else
                    {
                        single.onSuccess(Result.failure(Exception()))
                    }
                }

                override fun onError(e: Exception?) {
                    Log.e("\uD83D\uDD34", "Platform, AuthenticationUseCase,SignIn Error:", e)
                    single.onSuccess(Result.failure(e))
                }
            })
        }
        return observable.toObservable()
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