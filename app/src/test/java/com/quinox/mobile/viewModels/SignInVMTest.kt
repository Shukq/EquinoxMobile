package com.quinox.mobile.viewModels

import com.quinox.mobile.EquinoxTestCase
import io.reactivex.observers.TestObserver
import org.junit.Test

class SignInVMTest: EquinoxTestCase() {
    lateinit var vm: SignInVM.ViewModel
    val signInButtonIsEnabled = TestObserver<Boolean>()
    val loadingEnabled = TestObserver<Boolean>()
    val showError = TestObserver<String>()
    val signedInAction = TestObserver<Unit>()
    val forgotPasswordAction = TestObserver<Unit>()

    private fun setUpEnvironment(){
        val environment = environment()
        vm = SignInVM.ViewModel(environment)
        vm.outputs.loadingEnabled().subscribe(this.loadingEnabled)
        vm.outputs.showError().subscribe(this.showError)
        vm.outputs.signedInAction().subscribe(this.signedInAction)
        vm.outputs.signInButtonIsEnabled().subscribe(this.signInButtonIsEnabled)
        vm.outputs.forgotPasswordAction().subscribe(this.forgotPasswordAction)
    }

    @Test
    fun testButtonEnabled(){
        setUpEnvironment()
        vm.inputs.username("")
        vm.inputs.password("")
        this.signInButtonIsEnabled.assertValues(false)
        vm.inputs.username("jtoru@gmail.com")
        this.signInButtonIsEnabled.assertValues(false, false)
        vm.inputs.password("123Admin_")
        this.signInButtonIsEnabled.assertValues(false, false, true)
    }

    @Test
    fun testShowErrorMessage(){
        setUpEnvironment()
        vm.inputs.username("loktar@gmail.com")
        vm.inputs.password("pass123Wdsdsd_")
        vm.inputs.signInButtonPressed()
        this.showError.assertValueCount(1)
    }

    @Test
    fun testSignInAction(){
        setUpEnvironment()
        vm.inputs.username("loktar@gmail.com")
        vm.inputs.password("Memes1234_")
        vm.inputs.signInButtonPressed()
        this.signedInAction.assertValueCount(1)
    }

    @Test
    fun testForgotPassswordAction(){
        setUpEnvironment()
        vm.inputs.forgotPasswordPressed()
        forgotPasswordAction.assertValueCount(1)
    }

}