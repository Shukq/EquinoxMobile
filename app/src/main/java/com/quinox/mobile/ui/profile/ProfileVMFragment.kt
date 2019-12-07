package com.quinox.mobile.ui.profile

import androidx.annotation.NonNull
import com.quinox.mobile.base.FragmentViewModel
import com.quinox.mobile.libs.Environment
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface ProfileVMFragment {
    interface Inputs {
        fun onCreate()
    }
    interface Outputs {
        fun attributesList() : Observable<List<Pair<String, String>>>
    }
    class ViewModel(@NonNull val environment: Environment) : FragmentViewModel<ProfileVMFragment>(environment), Inputs, Outputs {

        val inputs : Inputs = this
        val outputs : Outputs = this

        //Inputs
        private val onCreate = PublishSubject.create<Unit>()

        //Outputs
        private val attributesList = BehaviorSubject.create<List<Pair<String, String>>>()

        init {
            val event = onCreate
                .flatMap { getAttributes() }
                .share()

            event
                .subscribe(this.attributesList)
        }

        override fun onCreate() {
            return this.onCreate.onNext(Unit)
        }

        override fun attributesList(): Observable<List<Pair<String, String>>> = this.attributesList

        //Helpers
        private fun getAttributes() : Observable<List<Pair<String, String>>>{
            return environment.authenticationUseCase().getAttributes()
        }
    }
}