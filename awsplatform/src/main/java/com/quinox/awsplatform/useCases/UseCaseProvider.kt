package com.quinox.awsplatform.useCases

import android.content.Context
import android.util.Log
import com.amazonaws.mobile.client.AWSMobileClient
import com.amazonaws.mobile.client.Callback
import com.amazonaws.mobile.client.UserStateDetails
import com.quinox.domain.useCases.AuthenticationUseCase
import com.quinox.domain.useCases.UseCaseProvider
import java.lang.Exception

class UseCaseProvider (context : Context) : UseCaseProvider{

    init {
        //Init clients or something we need
        AWSMobileClient.getInstance().initialize(context,object : Callback<UserStateDetails>{
            override fun onResult(result: UserStateDetails?) {
                Log.i("INIT", "onResult: " + result?.userState)
            }

            override fun onError(e: Exception?) {
                Log.e("INIT", "Initialization error.", e)            }
        })
    }

    override fun makeAuthenticationUseCase(): AuthenticationUseCase {
        return AuthenticationUseCase()
    }

}