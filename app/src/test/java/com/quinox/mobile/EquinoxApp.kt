package com.quinox.mobile

import android.app.Application
import android.content.Context

class EquinoxApp : Application() {
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