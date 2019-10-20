package com.quinox.mobile.appMod

import com.quinox.mobile.libs.Environment

interface ApplicationGraph {
    fun environment(): Environment
    fun inject(app: EquinoxApp)
}
