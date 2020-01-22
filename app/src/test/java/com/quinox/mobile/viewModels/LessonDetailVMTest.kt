package com.quinox.mobile.viewModels

import android.content.Intent
import com.quinox.domain.entities.ContentfulClass
import com.quinox.mobile.EquinoxTestCase
import io.reactivex.observers.TestObserver
import org.junit.Test

class LessonDetailVMTest: EquinoxTestCase() {
    lateinit var vm : LessonDetailVM.ViewModel
    private val loadInfo = TestObserver<ContentfulClass>()


    private fun setUpEnvironment() {
        val environment = environment()
        vm = LessonDetailVM.ViewModel(environment)
        val contentfulClass = ContentfulClass("123", "title", null, mutableListOf(), null)
        vm.intent(Intent().putExtra("clase", contentfulClass))
        vm.output.loadInfo().subscribe(loadInfo)
    }

    @Test
    fun testLoadInfo(){
        setUpEnvironment()
        loadInfo.assertValueCount(1)
    }
}