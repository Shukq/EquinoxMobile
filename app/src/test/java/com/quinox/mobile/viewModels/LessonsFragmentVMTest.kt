package com.quinox.mobile.viewModels

import com.quinox.mobile.EquinoxTestCase
import com.quinox.mobile.ui.lessons.LessonsVM

class LessonsFragmentVMTest : EquinoxTestCase() {
    lateinit var vm : LessonsVM.ViewModel

    private fun setUpEnviroment(){
        val environment = environment()
        vm = LessonsVM.ViewModel(environment)
    }
}