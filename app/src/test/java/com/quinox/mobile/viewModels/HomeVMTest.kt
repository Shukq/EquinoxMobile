package com.quinox.mobile.viewModels

import com.quinox.mobile.EquinoxTestCase
import io.reactivex.observers.TestObserver
import org.junit.Test

class HomeVMTest : EquinoxTestCase(){
    lateinit var vm : HomeVM.ViewModel
    private val signOutAction = TestObserver<Unit>()
    private val profileInfo = TestObserver<List<Pair<String, String>>>()

    private fun setUpEnvironment(){
        val environment = environment()
        vm = HomeVM.ViewModel(environment)
        vm.outputs.profileInfo().subscribe(profileInfo)
        vm.outputs.signOutAction().subscribe(signOutAction)
    }

    @Test
    fun testSignOutAction(){
        setUpEnvironment()
        vm.inputs.signOutButtonPressed()
        signOutAction.assertValueCount(1)
    }

    @Test
    fun testProfileInfo(){
        setUpEnvironment()
        vm.inputs.onCreate()
        profileInfo.assertValueCount(1)
    }
}