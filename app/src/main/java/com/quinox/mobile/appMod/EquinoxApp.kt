package com.quinox.mobile.appMod

import android.app.Application
import android.content.Context

class EquinoxApp : Application() {
    private var component: ApplicationComponent ? = null

    override fun onCreate() {
        super.onCreate()
        component = DaggerApplicationComponent.builder()
            .applicationModule(ApplicationModule(this))
            .build()

        this.component()?.inject(this)
    }

    fun component(): ApplicationComponent? {
        return this.component
    }

    init {
        instance = this
    }

    companion object {
        private var instance: EquinoxApp? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
}