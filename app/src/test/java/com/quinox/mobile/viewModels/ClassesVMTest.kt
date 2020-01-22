package com.quinox.mobile.viewModels

import android.content.Intent
import com.quinox.domain.entities.ContentfulClass
import com.quinox.domain.entities.ContentfulSection
import com.quinox.mobile.EquinoxTestCase
import io.reactivex.observers.TestObserver
import org.junit.Test

class ClassesVMTest : EquinoxTestCase(){
    lateinit var vm: ClassesVM.ViewModel
    private val classPicker = TestObserver<ContentfulClass>()
    private val section = TestObserver<ContentfulSection>()
    private val classList = TestObserver<List<ContentfulClass>>()
    private val showError = TestObserver<String>()

    private fun setUpEnvironment(){
        val environment = environment()
        vm =  ClassesVM.ViewModel(environment)
        val sectionModel = ContentfulSection("123","title","123", mutableListOf())
        vm.intent(Intent().putExtra("section",sectionModel))
        vm.outputs.classPicker().subscribe(classPicker)
        vm.outputs.section().subscribe(section)
        vm.outputs.classList().subscribe(classList)
        vm.outputs.showError().subscribe(showError)
    }

    private fun setUpErrorEnvironment(){
        val environment = environment()
        vm =  ClassesVM.ViewModel(environment)
        val sectionModel = ContentfulSection("123456","title","123", mutableListOf())
        vm.intent(Intent().putExtra("section",sectionModel))
        vm.outputs.classPicker().subscribe(classPicker)
        vm.outputs.section().subscribe(section)
        vm.outputs.classList().subscribe(classList)
        vm.outputs.showError().subscribe(showError)
    }

    @Test
    fun testClassPicker(){
        setUpEnvironment()
        val contentfulClass = ContentfulClass("123","title", null, mutableListOf(), null)
        vm.inputs.classClicked(contentfulClass)
        classPicker.assertValue(contentfulClass)
    }

    @Test
    fun testSection(){
        setUpEnvironment()
        section.assertValueCount(1)
    }

    @Test
    fun testClassList(){
        setUpEnvironment()
        classList.assertValueCount(1)
    }

    @Test
    fun testShowError(){
        setUpErrorEnvironment()
        showError.assertValueCount(1)
    }

}