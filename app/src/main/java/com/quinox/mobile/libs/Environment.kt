package com.quinox.mobile.libs

import android.content.SharedPreferences
import android.os.Parcelable
import auto.parcel.AutoParcel
import com.quinox.domain.useCases.AuthenticationUseCase

@AutoParcel
abstract class Environment : Parcelable {
    abstract fun authenticationUseCase(): AuthenticationUseCase
    abstract fun sharedPreferences(): SharedPreferences
    @AutoParcel.Builder
    abstract class Builder {
        abstract fun authenticationUseCase(authenticationUseCase: AuthenticationUseCase) : Builder
        abstract fun sharedPreferences(sharedPreferences: SharedPreferences): Builder
        abstract fun build() : Environment
    }

    abstract fun toBuilder(): Builder

    companion object {

        fun builder(): Builder {
            return AutoParcel_Environment.Builder()
        }
    }
}
