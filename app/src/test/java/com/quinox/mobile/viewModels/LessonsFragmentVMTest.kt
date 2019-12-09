package com.quinox.mobile.viewModels

import com.quinox.domain.entities.ContentfulUnit
import com.quinox.mobile.EquinoxTestCase
import com.quinox.mobile.ui.lessons.LessonsVM
import io.reactivex.observers.TestObserver
import org.junit.Test

class LessonsFragmentVMTest : EquinoxTestCase() {
    lateinit var vm : LessonsVM.ViewModel
    private val showError = TestObserver<String>()
    private val loading = TestObserver<Boolean>()
    private val showInfo = TestObserver<List<ContentfulUnit>>()

    private fun setUpEnvironment(){
        val environment = environment()
        vm = LessonsVM.ViewModel(environment)
        vm.outputs.loading().subscribe(loading)
        vm.outputs.showError().subscribe(showError)
        vm.outputs.showInfo().subscribe(showInfo)

        vm.outputs.showError().subscribe {
            println(it)
        }

        vm.outputs.showInfo().subscribe {
            it.forEach {
                println(it.title)
                println(it.sections.toString())
            }
        }
    }

    @Test
    fun testShowInfo(){
        setUpEnvironment()
        vm.inputs.onCreate()
        showInfo.assertValueCount(1)
    }
}