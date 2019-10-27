package com.quinox.mobile.viewModels

import com.quinox.mobile.EquinoxTestCase
import io.reactivex.observers.TestObserver
import org.junit.Test

class GetSessionVMTest : EquinoxTestCase() {
    lateinit var vm : GetSessionVM.ViewModel
    private val signedOutAction = TestObserver<Unit>()

    private fun setUpEnvironment(){
        val environment = environment()
        vm = GetSessionVM.ViewModel(environment)
        vm.outputs.signedOutAction().subscribe(signedOutAction)
    }

    @Test
    fun testSignOutAction(){
        setUpEnvironment()
        vm.inputs.onCreate()
        signedOutAction.assertValueCount(1)
    }
}