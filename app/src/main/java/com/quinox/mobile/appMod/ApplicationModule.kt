package com.quinox.mobile.appMod

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import androidx.annotation.NonNull
import com.quinox.awsplatform.useCases.UseCaseProvider
import com.quinox.domain.useCases.AuthenticationUseCase
import com.quinox.mobile.libs.Environment
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(@NonNull val application: Application) {
    private val awsProvider = UseCaseProvider(application.applicationContext)

    @Provides
    @Singleton
    fun provideEnvironment(
        @NonNull authenticationUseCase: AuthenticationUseCase,
        @NonNull sharedPreferences: SharedPreferences
    ): Environment {
        return Environment.builder()
            .authenticationUseCase(authenticationUseCase)
            .sharedPreferences(sharedPreferences)
            .build()
    }

    @Provides
    @Singleton
    fun provideSharedPreferences(): SharedPreferences {
        return application.getSharedPreferences("SP", Activity.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideAuthenticationUseCase() : AuthenticationUseCase{
        return awsProvider.makeAuthenticationUseCase()
    }
}