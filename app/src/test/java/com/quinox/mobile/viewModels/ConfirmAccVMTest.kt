package com.quinox.mobile.viewModels

import android.content.Intent
import com.quinox.domain.entities.Gender
import com.quinox.domain.entities.SignUpModel
import com.quinox.domain.entities.SignUpResult
import com.quinox.domain.entities.SignUpState
import com.quinox.mobile.EquinoxTestCase
import io.reactivex.observers.TestObserver
import org.junit.Test

class ConfirmAccVMTest: EquinoxTestCase() {
    lateinit var vm: ConfirmAccVM.ViewModel
    private val acceptButtonEnabled = TestObserver<Boolean>()
    private val showError = TestObserver<String>()
    private val loadingEnabled = TestObserver<Boolean>()
    private val resendButtonAction = TestObserver<Boolean>()
    private val acceptButtonAction = TestObserver<Unit>()

    private fun setUpEnvironment(){
        val environment = environment()
        vm = ConfirmAccVM.ViewModel(environment)
        val model = SignUpResult(SignUpState.unknown,"loktar@gmail.com","Memes1234_")
        vm.intent(Intent().putExtra("SignUpResult",model))
        vm.outputs.acceptButtonEnabled().subscribe(acceptButtonEnabled)
        vm.outputs.showError().subscribe(showError)
        vm.outputs.loadingEnabled().subscribe(loadingEnabled)
        vm.outputs.resendButtonAction().subscribe(resendButtonAction)
        vm.outputs.acceptButtonAction().subscribe(acceptButtonAction)
    }

    @Test
    fun testAcceptButtonEnabled(){
        setUpEnvironment()
        vm.inputs.code("123456")
        acceptButtonEnabled.assertValues(true)
        vm.inputs.code("123")
        acceptButtonEnabled.assertValues(true,false)
        vm.inputs.code("asd123")
        acceptButtonEnabled.assertValues(true,false,false)
    }

    @Test
    fun testShowError(){
        setUpEnvironment()
        val model = SignUpResult(SignUpState.unconfirmed,"loktar@gmail.com","Memes1234")
        vm.intent(Intent().putExtra("SignUpResult",model))
        vm.inputs.code("123456")
        vm.inputs.acceptButtonPressed()
        showError.assertValueCount(1)
    }

    @Test
    fun testLoadingEnabled(){
        setUpEnvironment()
        vm.inputs.code("123456")
        vm.inputs.acceptButtonPressed()
        loadingEnabled.assertValues(true,false)
    }

    @Test
    fun testResendButtonAction(){
        setUpEnvironment()
        vm.inputs.resendButtonPressed()
        resendButtonAction.assertValue(true)
    }

    @Test
    fun testAcceptButtonAction(){
        setUpEnvironment()
        vm.inputs.code("123456")
        vm.inputs.acceptButtonPressed()
        acceptButtonAction.assertValueCount(1)
    }
}