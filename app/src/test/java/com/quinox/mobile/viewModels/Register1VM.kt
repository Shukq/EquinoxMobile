package com.quinox.mobile.viewModels

import com.quinox.domain.entities.Gender
import com.quinox.domain.entities.SignUpModel
import com.quinox.mobile.EquinoxTestCase
import io.reactivex.observers.TestObserver
import org.junit.Test

class Register1VMTest: EquinoxTestCase() {
    lateinit var vm: Register1VM.ViewModel
    private val dateTextChanged = TestObserver<String>()
    private val openDatePicker = TestObserver<Unit>()
    private val nextButtonIsEnabled = TestObserver<Boolean>()
    private val register1Action =  TestObserver<SignUpModel>()

    private fun setUpEnvironment(){
        val environment = environment()
        vm = Register1VM.ViewModel(environment)
        vm.outputs.dateTextChanged().subscribe(dateTextChanged)
        vm.outputs.openDatePicker().subscribe(openDatePicker)
        vm.outputs.nextButtonIsEnabled().subscribe(nextButtonIsEnabled)
        vm.outputs.register1Action().subscribe(register1Action)
    }

    @Test
    fun testDateTextChanged(){
        setUpEnvironment()
        vm.inputs.dateActionReceive("2012-11-28")
        dateTextChanged.assertValue("2012-11-28")
    }

    @Test
    fun testOpenDatePicker(){
        setUpEnvironment()
        vm.inputs.datePressed()
        openDatePicker.assertValueCount(1)
    }

    @Test
    fun testNextButtonIsEnabled(){
        setUpEnvironment()
        vm.inputs.email("loktar@gmail.com")
        vm.inputs.dateActionReceive("2012-11-28")
        vm.inputs.gender(Gender.male)
        vm.inputs.username("Jtoru")
        vm.inputs.occupation("random")
        nextButtonIsEnabled.assertValue(true)
    }

    @Test
    fun testRegister1Action(){
        setUpEnvironment()
        vm.inputs.email("loktar@gmail.com")
        vm.inputs.dateActionReceive("2012-11-28")
        vm.inputs.gender(Gender.male)
        vm.inputs.username("Jtoru")
        vm.inputs.occupation("random")
        vm.inputs.nextButtonPressed()
        register1Action.assertValueCount(1)
    }
}