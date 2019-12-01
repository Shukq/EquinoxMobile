package com.quinox.mobile.ui.home

import androidx.annotation.NonNull
import com.quinox.domain.entities.Result
import com.quinox.mobile.base.FragmentViewModel
import com.quinox.mobile.libs.Environment
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface HomeVMFragment {
    interface Inputs{
        fun onCreate()
    }
    interface Outputs{
        fun loadContent() : Observable<String>
        fun showError() : Observable<Unit>
    }
    class ViewModel(@NonNull val environment: Environment) : FragmentViewModel<HomeVMFragment>(environment), Inputs, Outputs{
        val inputs : Inputs = this
        val outputs : Outputs = this

        //Inputs
        private val onCreate = PublishSubject.create<Unit>()

        //Ouputs
        private val loadContent = BehaviorSubject.create<String>()
        private val showError = BehaviorSubject.create<Unit>()

        init {
            val createPage = onCreate
                .flatMap { return@flatMap this.getHcmePageContent()}
                .share()
            createPage
                .filter { it.isFail() }
                .map { Unit }
                .subscribe(this.showError)
            createPage
                .filter { !it.isFail() }
                .map { it.successValue() }
                .subscribe(this.loadContent)
        }

        override fun onCreate() {
            return this.onCreate.onNext(Unit)
        }

        override fun loadContent() : Observable<String> = this.loadContent
        override fun showError(): Observable<Unit> = this.showError
        private fun getHcmePageContent():Observable<Result<String>>{
            return environment.contentfulUseCase().getHomePage()
        }
    }


}