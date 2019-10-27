package com.quinox.mobile.viewModels

import com.quinox.mobile.EquinoxTestCase
import io.reactivex.observers.TestObserver
import org.junit.Test

class MainVMTest : EquinoxTestCase() {
    lateinit var vm : MainVM.ViewModel
    private val signInNav = TestObserver<Unit>()
    private val signUpNav = TestObserver<Unit>()

    private fun setUpEnvironment(){
        val environment = environment()
        vm = MainVM.ViewModel(environment)
        vm.output.signInNav().subscribe(signInNav)
        vm.output.signUpNav().subscribe(signUpNav)
    }

    @Test
    fun testSignIn(){
        setUpEnvironment()
        vm.input.signInBtnPressed()
        signInNav.assertValueCount(1)
    }

    @Test
    fun testSignUp(){
        setUpEnvironment()
        vm.input.signUpBtnPressed()
        signUpNav.assertValueCount(1)
    }

}