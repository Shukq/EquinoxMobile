package com.quinox.mobile.appMod

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.annotation.NonNull
import com.quinox.awsplatform.useCases.UseCaseProvider
import com.quinox.domain.useCases.AuthenticationUseCase
import com.quinox.domain.useCases.ContentfulUseCase
import com.quinox.mobile.libs.Environment
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class ApplicationModule(@NonNull val application: Application) {
    private val provider = UseCaseProvider(application.applicationContext)
    @Provides
    @Singleton
    fun provideEnvironment(
        @NonNull authenticationUseCase: AuthenticationUseCase,
        @NonNull sharedPreferences: SharedPreferences,
        @NonNull contentfulUseCase: ContentfulUseCase,
        @NonNull context: Context
    ): Environment {
        return Environment.builder()
            .authenticationUseCase(authenticationUseCase)
            .sharedPreferences(sharedPreferences)
            .contentfulUseCase(contentfulUseCase)
            .context(context)
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
        return provider.makeAuthenticationUseCase()
    }

    @Provides
    @Singleton
    fun provideContentfulUseCase() : ContentfulUseCase{
        return provider.makeContentfulUseCase()
    }

    @Provides
    @Singleton
    fun provideContext() : Context{
        return application.applicationContext
    }
}