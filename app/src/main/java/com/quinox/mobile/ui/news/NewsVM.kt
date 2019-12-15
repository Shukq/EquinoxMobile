package com.quinox.mobile.ui.news

import android.util.Log
import androidx.annotation.NonNull
import com.quinox.domain.entities.ContentfulNews
import com.quinox.domain.entities.ContentfulSection
import com.quinox.domain.entities.ContentfulUnit
import com.quinox.domain.entities.Result
import com.quinox.mobile.base.FragmentViewModel
import com.quinox.mobile.libs.Environment
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.lang.Exception

interface NewsVM {
    interface Inputs {
        fun onCreate()
        fun retry()
    }
    interface Outputs {
        fun showError() : Observable<String>
        fun loading() : Observable<Boolean>
        fun showInfo() : Observable<List<ContentfulNews>>
    }

    class ViewModel(@NonNull val environment: Environment) : FragmentViewModel<NewsVM>(environment), Inputs, Outputs{

        val inputs : Inputs = this
        val outputs: Outputs = this

        //Inputs
        private val onCreate = PublishSubject.create<Unit>()
        private val retry = PublishSubject.create<Unit>()

        //Outputs
        private val showError = BehaviorSubject.create<String>()
        private val loading = BehaviorSubject.create<Boolean>()
        private val showInfo = BehaviorSubject.create<List<ContentfulNews>>()

        init {
            val createEvent = onCreate
                .flatMap {
                    return@flatMap eventServer()
                }
                .share()

            createEvent
                .filter { it.isFail() }
                .map { "No se pudo cargar el contenido" }
                .subscribe(showError)

            createEvent
                .filter { !it.isFail() }
                .map { it.successValue() }
                .subscribe(showInfo)

        }

        override fun onCreate() {
            return this.onCreate.onNext(Unit)
        }

        override fun retry() {
            return this.retry.onNext(Unit)
        }


        override fun showError(): Observable<String> = this.showError

        override fun loading(): Observable<Boolean> = this.loading

        override fun showInfo(): Observable<List<ContentfulNews>> = this.showInfo

        private fun eventServer() : Observable<Result<List<ContentfulNews>>> {

            return environment.contentfulUseCase().getNews()
        }

    }

}