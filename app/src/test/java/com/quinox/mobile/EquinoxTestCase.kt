package com.quinox.mobile

import android.app.Activity
import android.app.Application
import android.content.SharedPreferences
import com.quinox.domain.useCases.AuthenticationUseCase
import com.quinox.mobile.libs.Environment
import io.reactivex.annotations.NonNull
import junit.framework.TestCase
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config

@RunWith(EquinoxGradleTestRunner::class)
@Config(manifest = Config.NONE, sdk = [EquinoxGradleTestRunner.DEFAULT_SDK],application = TestApp::class)
abstract class EquinoxTestCase  : TestCase(){

    private var application : Application ? = null
    lateinit var environment: Environment
    @Before
    @Throws(Exception::class)
    public override fun setUp() {
        super.setUp()

        val environment = Environment.builder()
            .authenticationUseCase(authenticationUseCase())
            .sharedPreferences(sharedPref())
            .build()
        this.environment = environment
    }

    @NonNull
    protected fun application(): Application? {
        if (this.application != null) {
            return this.application
        }
        this.application = RuntimeEnvironment.application
        //this.application = RuntimeEnvironment.application as TestRPApp
        return this.application
    }

    private fun sharedPref(): SharedPreferences {
        return application()?.applicationContext?.getSharedPreferences("SP", Activity.MODE_PRIVATE)!!
    }

    private fun authenticationUseCase() : AuthenticationUseCase{
        return com.quinox.mobile.mocks.AuthenticationUseCase()
    }

    @NonNull
    protected fun environment(): Environment {
        return this.environment
    }
}