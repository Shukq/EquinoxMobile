package com.quinox.mobile.viewModels

import com.quinox.mobile.EquinoxTestCase
import io.reactivex.observers.TestObserver
import org.junit.Test

class ForgotPasswordVMTest : EquinoxTestCase() {
    lateinit var vm: ForgotPasswordVM.ViewModel
    private val nextButtonEnabled = TestObserver<Boolean>()
    private val loadingEnabled = TestObserver<Boolean>()
    private val showError = TestObserver<String>()
    private val forgotPasswordStatus = TestObserver<String>()

    private fun setUpEnvironment(){
        val environment = environment()
        vm = ForgotPasswordVM.ViewModel(environment)
        vm.outputs.nextButtonEnabled().subscribe(nextButtonEnabled)
        vm.outputs.loadingEnabled().subscribe(loadingEnabled)
        vm.outputs.showError().subscribe(showError)
        vm.outputs.forgotPasswordStatus().subscribe(forgotPasswordStatus)
    }

    @Test
    fun testNextBtnEnabled(){
        setUpEnvironment()
        vm.inputs.usernameChanged("loktar@gmail.com")
        nextButtonEnabled.assertValue(true)
    }

    @Test
    fun testLoadingEnabled(){
        setUpEnvironment()
        vm.inputs.usernameChanged("loktar@gmail.com")
        vm.inputs.nextButtonPressed()
        loadingEnabled.assertValues(true, false)
    }

    @Test
    fun testShowError(){
        setUpEnvironment()
        vm.inputs.usernameChanged("random@gmail.com")
        vm.inputs.nextButtonPressed()
        showError.assertValueCount(1)
    }

    @Test
    fun testForgotPasswordStatus(){
        setUpEnvironment()
        vm.inputs.usernameChanged("loktar@gmail.com")
        vm.inputs.nextButtonPressed()
        forgotPasswordStatus.assertValueCount(1)
    }
}