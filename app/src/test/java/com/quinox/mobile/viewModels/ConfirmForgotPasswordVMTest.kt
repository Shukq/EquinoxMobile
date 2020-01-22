package com.quinox.mobile.viewModels

import com.quinox.mobile.EquinoxTestCase
import io.reactivex.observers.TestObserver
import org.junit.Test

class ConfirmForgotPasswordVMTest: EquinoxTestCase() {
    lateinit var vm: ConfirmForgotPasswordVM.ViewModel
    private val nextButtonEnabled = TestObserver<Boolean>()
    private val passwordChangedAction = TestObserver<Boolean>()
    private val showError = TestObserver<String>()
    private val loadingEnabled = TestObserver<Boolean>()

    private fun setUpEnvironment(){
        val environment = environment()
        vm = ConfirmForgotPasswordVM.ViewModel(environment)
        vm.outputs.nextButtonEnabled().subscribe(nextButtonEnabled)
        vm.outputs.passwordChangedAction().subscribe(passwordChangedAction)
        vm.outputs.showError().subscribe(showError)
        vm.outputs.loadingEnabled().subscribe(loadingEnabled)
    }

    @Test
    fun testNextButtonEnabled(){
        setUpEnvironment()
        vm.inputs.confirmationCodeTextChanged("123456")
        vm.inputs.passwordChanged("123Admin_")
        nextButtonEnabled.assertValues(true)
    }

    @Test
    fun testPasswordChangedAction(){
        setUpEnvironment()
        vm.inputs.confirmationCodeTextChanged("123456")
        vm.inputs.passwordChanged("123Admin_")
        vm.inputs.nextButtonPressed()
        passwordChangedAction.assertValue(true)
    }

    @Test
    fun testShowError(){
        setUpEnvironment()
        vm.inputs.confirmationCodeTextChanged("122456")
        vm.inputs.passwordChanged("123Admin_")
        vm.inputs.nextButtonPressed()
        showError.assertValueCount(1)
    }

    @Test
    fun testLoading(){
        setUpEnvironment()
        vm.inputs.confirmationCodeTextChanged("123456")
        vm.inputs.passwordChanged("123Admin_")
        vm.inputs.nextButtonPressed()
        loadingEnabled.assertValues(true, false)
    }
}