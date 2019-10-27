package com.quinox.mobile.viewModels

import android.content.Intent
import com.quinox.domain.entities.Gender
import com.quinox.domain.entities.SignUpModel
import com.quinox.domain.entities.SignUpResult
import com.quinox.domain.entities.SignUpState
import com.quinox.mobile.EquinoxTestCase
import io.reactivex.observers.TestObserver
import org.junit.Test
import kotlin.math.sign

class Register2VMTest : EquinoxTestCase() {
    lateinit var vm: Register2VM.ViewModel
    private val signUpButtonEnabled = TestObserver<Boolean>()
    private val validPasswordLenght = TestObserver<Boolean>()
    private val validPasswordCapitalLowerLetters = TestObserver<Boolean>()
    private val validPasswordNumbers = TestObserver<Boolean>()
    private val validPasswordSpecialCharacters = TestObserver<Boolean>()
    private val showError = TestObserver<String>()
    private val loadingEnabled = TestObserver<Boolean>()
    private val confirmAccAction = TestObserver<SignUpResult>()

    private fun setUpEnvironment(){
        val environment = environment()
        vm = Register2VM.ViewModel(environment)
        val model = SignUpModel("Toruto Uzumaki","loktar@gmail.com","SOS UN MEME","wefweff", Gender.female)
        vm.intent(Intent().putExtra("model",model))
        vm.outputs.signUpButtonEnabled().subscribe(signUpButtonEnabled)
        vm.outputs.validPasswordLenght().subscribe(validPasswordLenght)
        vm.outputs.validPasswordCapitalLowerLetters().subscribe(validPasswordCapitalLowerLetters)
        vm.outputs.validPasswordNumbers().subscribe(validPasswordNumbers)
        vm.outputs.validPasswordSpecialCharacters().subscribe(validPasswordSpecialCharacters)
        vm.outputs.showError().subscribe(showError)
        vm.outputs.loadingEnabled().subscribe(loadingEnabled)
        vm.outputs.confirmAccAction().subscribe(confirmAccAction)
    }

    @Test
    fun testSignUpButtonEnable(){
        setUpEnvironment()
        vm.inputs.password("Memes1234_")
        signUpButtonEnabled.assertValue(true)
    }

    @Test
    fun passLenght(){
        setUpEnvironment()
        this.vm.inputs.password("123")
        this.validPasswordLenght.assertValues(false)
        this.vm.inputs.password("12345678")
        this.validPasswordLenght.assertValues(false, true)
        this.vm.inputs.password("12345678qwertyuiopasd")
        this.validPasswordLenght.assertValues(false, true, false)
        this.vm.inputs.password("12345678qwertyuiopas")
        this.validPasswordLenght.assertValues(false, true, false, true)
    }

    @Test
    fun passCapitalLower(){
        setUpEnvironment()
        this.vm.inputs.password("aaa")
        this.validPasswordCapitalLowerLetters.assertValues(false)
        this.vm.inputs.password("aaaB")
        this.validPasswordCapitalLowerLetters.assertValues(false, true)
    }

    @Test
    fun passNumbers(){
        setUpEnvironment()
        this.vm.inputs.password("aaa")
        this.validPasswordNumbers.assertValues(false)
        this.vm.inputs.password("aaa1")
        this.validPasswordNumbers.assertValues(false, true)
    }

    @Test
    fun passSpecialCharacters(){
        setUpEnvironment()
        this.vm.inputs.password("aaa")
        this.validPasswordSpecialCharacters.assertValues(false)
        this.vm.inputs.password("aaa$$")
        this.validPasswordSpecialCharacters.assertValues(false, true)

    }

    @Test
    fun testLoadingEnabled(){
        setUpEnvironment()
        vm.inputs.password("Memes1234_")
        vm.inputs.signUpButtonPressed()
        loadingEnabled.assertValues(true,false)
    }

    @Test
    fun testConfirmAccAction(){
        setUpEnvironment()
        vm.inputs.password("Memes1234_")
        val model = SignUpResult(SignUpState.confirmed,"loktar@gmail.com","Memes1234_")
        vm.inputs.signUpButtonPressed()
        confirmAccAction.assertValue(model)
    }

    @Test
    fun testShowError(){
        setUpEnvironment()
        val model = SignUpModel("Toruto Uzumaki","lreg@gmail.com","SOS UN MEME","wefweff", Gender.female)
        vm.intent(Intent().putExtra("model",model))
        vm.inputs.password("Memes1234_")
        vm.inputs.signUpButtonPressed()
        showError.assertValueCount(1)
    }
}