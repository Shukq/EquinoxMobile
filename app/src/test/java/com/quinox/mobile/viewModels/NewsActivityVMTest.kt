package com.quinox.mobile.viewModels

import android.content.Intent
import com.quinox.domain.entities.ContentfulNews
import com.quinox.mobile.EquinoxTestCase
import io.reactivex.observers.TestObserver
import org.junit.Test

class NewsActivityVMTest: EquinoxTestCase() {
    lateinit var vm : NewsActivityVM.ViewModel
    private val loadInfo = TestObserver<ContentfulNews>()

    private fun setUpEnvironment() {
        val environment = environment()
        vm = NewsActivityVM.ViewModel(environment)
        val contentfulClass = ContentfulNews("123", "title","",null)
        vm.intent(Intent().putExtra("noticia", contentfulClass))
        vm.outputs.loadInfo().subscribe(loadInfo)
    }

    @Test
    fun testLoadInfo(){
        setUpEnvironment()
        loadInfo.assertValueCount(1)
    }
}