package com.quinox.awsplatform.useCases

import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserState
import com.amazonaws.mobile.client.results.ForgotPasswordResult
import com.amazonaws.mobile.client.results.ForgotPasswordState
import com.quinox.domain.entities.*
import com.quinox.domain.useCases.AuthenticationUseCase
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.Exception

class AuthenticationUseCase : AuthenticationUseCase {
    override fun initForgotPassword(email : String) : Observable<Result<String>>{
        val single = Single.create<Result<String>> create@{ single ->
            AWSMobileClient.getInstance().forgotPassword(email, object: Callback<ForgotPasswordResult>{
                override fun onResult(result: ForgotPasswordResult?) {
                    val response = result?.parameters
                    if(response != null){
                        Log.e("ForgotPassword",response.deliveryMedium)
                        Log.e("ForgotPassword",response.destination)
                        single.onSuccess(Result.success(response.destination))
                    }
                    else{
                        single.onSuccess(Result.failure(Exception()))
                    }
                }

                override fun onError(e: Exception?) {
                    Log.e("\uD83D\uDD34", "Platform, AuthenticationUseCase,initForgotPassword Error:", e)
                    single.onSuccess(Result.failure(e))
                }

            })
        }
        return single.toObservable()
    }

    override fun confirmForgotPassword(newPassword: String, code: String) : Observable<Result<Boolean>>{
        val single = Single.create<Result<Boolean>> create@{ single ->
            AWSMobileClient.getInstance().confirmForgotPassword(newPassword, code,  object: Callback<ForgotPasswordResult>{
                override fun onResult(result: ForgotPasswordResult?) {
                    if(result!= null) {
                        Log.e("confirmForgotPassword",result.state.name)
                        val confirmation = when(result.state){
                            ForgotPasswordState.DONE -> true
                            else -> false
                        }
                        single.onSuccess(Result.success(confirmation))
                    }
                    else{
                        single.onSuccess(Result.failure(Exception()))
                    }
                }

                override fun onError(e: Exception?) {
                    Log.e("\uD83D\uDD34", "Platform, AuthenticationUseCase,confirmForgotPassword Error:", e)
                    single.onSuccess(Result.failure(e))
                }

            })
        }
        return single.toObservable()
    }

    override fun getAttributes(): Observable<List<Pair<String, String>>> {
        val single = Single.create<List<Pair<String, String>>> create@{ single ->
           AWSMobileClient.getInstance().getUserAttributes(object:Callback<Map<String,String>>{
               override fun onResult(result: Map<String, String>?) {
                   var list : List<Pair<String,String>> = mutableListOf()
                   if(result!=null){
                       list = result.toList()
                   }
                   single.onSuccess(list)
               }

               override fun onError(e: Exception?) {
                   Log.e("\uD83D\uDD34", "Platform, AuthenticationUseCase,getAttributes Error:", e)
                   single.onSuccess(mutableListOf())
               }

           })
        }
        return single.toObservable()
    }

    override fun resendConfirmationCode(username: String): Observable<Result<Boolean>> {
        val single = Single.create<Result<Boolean>> create@{ single ->
            AWSMobileClient.getInstance().resendSignUp(username,object : Callback<com.amazonaws.mobile.client.results.SignUpResult>{
                override fun onResult(result: com.amazonaws.mobile.client.results.SignUpResult?) {
                    if (result!=null){
                        if(!result.confirmationState){
                            single.onSuccess(Result.success(true))

                        }else{
                            single.onSuccess(Result.success(false))
                        }
                    }else{
                        single.onSuccess(Result.failure(Exception()))
                    }
                }

                override fun onError(e: Exception?) {
                    Log.e("\uD83D\uDD34", "Platform, AuthenticationUseCase,resendConfirmationCode Error:", e)
                    single.onSuccess(Result.failure(e))
                }
            } )
        }
        return single.toObservable()
    }

    override fun confirmSignUp(
        username: String,
        confirmationCode: String
    ): Observable<Result<Boolean>> {
        val single = Single.create<Result<Boolean>> create@{ single ->
            AWSMobileClient.getInstance().confirmSignUp(username,confirmationCode, object : Callback<com.amazonaws.mobile.client.results.SignUpResult>{
                override fun onResult(result: com.amazonaws.mobile.client.results.SignUpResult?) {
                    if (result!=null){
                        if(!result.confirmationState){
                            single.onSuccess(Result.success(false))

                        }else{
                            single.onSuccess(Result.success(true))
                        }
                    }
                }

                override fun onError(e: Exception?) {
                    Log.e("\uD83D\uDD34", "Platform, AuthenticationUseCase,confirmSignUp Error:", e)
                    single.onSuccess(Result.failure(e))
                }

            })
        }
        return single.toObservable()
    }

    override fun getCurrentUserState(): Observable<UserStateResult> {
        val single = Single.create<UserStateResult> create@{ single ->
            AWSMobileClient.getInstance().currentUserState().userState
            Log.e("UserState", AWSMobileClient.getInstance().currentUserState().userState.toString())
            val state = when(AWSMobileClient.getInstance().currentUserState().userState){
                UserState.SIGNED_IN -> UserStateResult.signedIn
                UserState.SIGNED_OUT, UserState.GUEST, UserState.UNKNOWN, UserState.SIGNED_OUT_FEDERATED_TOKENS_INVALID,
                UserState.SIGNED_OUT_USER_POOLS_TOKENS_INVALID -> UserStateResult.signedOut
                else -> UserStateResult.signedOut
            }
            single.onSuccess(state)
        }
        return single.toObservable()
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
        val single = Single.create<Result<UserStateResult>> create@{ single ->
            AWSMobileClient.getInstance().signOut()
            single.onSuccess(Result.success(UserStateResult.signedOut))
        }
        return single.toObservable()
    }

    override fun signUp(
        username: String,
        password: String,
        name: String,
        gender: Gender,birthDate: String, occupation: String
    ): Observable<Result<SignUpResult>> {
        val single = Single.create<Result<SignUpResult>> create@{ single ->
            val userGender : String = when(gender){
                Gender.female -> "female"
                Gender.male -> "male"
                else -> "other"
            }
            AWSMobileClient.getInstance().signUp(username,password, mapOf(Pair("email",username),Pair("name",name),Pair("gender",userGender),Pair("birthdate",birthDate),Pair("custom:occupation", occupation)),
                null, object: Callback<com.amazonaws.mobile.client.results.SignUpResult>{
                override fun onResult(result: com.amazonaws.mobile.client.results.SignUpResult?) {
                    val state : SignUpState
                    if (result!=null){
                        if(!result.confirmationState){
                            state = SignUpState.unconfirmed
                            val result1 = SignUpResult(state,username,password)
                            single.onSuccess(Result.success(result1))

                        }else{
                            state = SignUpState.confirmed
                            val result1 = SignUpResult(state,username,password)
                            single.onSuccess(Result.success(result1))
                        }
                    }

                }

                override fun onError(e: Exception?) {
                    Log.e("\uD83D\uDD34", "Platform, AuthenticationUseCase,SignUp Error:", e)
                    single.onSuccess(Result.failure(e))
                }

            })
        }
        return single.toObservable()
    }

}