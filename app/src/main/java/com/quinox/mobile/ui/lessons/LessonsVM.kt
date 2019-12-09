package com.quinox.mobile.ui.lessons

import androidx.annotation.NonNull
import com.quinox.domain.entities.ContentfulSection
import com.quinox.domain.entities.ContentfulUnit
import com.quinox.mobile.appMod.EquinoxApp
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.libs.Environment
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import com.quinox.domain.entities.Result
import java.lang.Exception

interface LessonsVM {
    interface Inputs {
        fun onCreate()
        fun retry()
    }
    interface Outputs {
        fun showError() : Observable<String>
        fun loading() : Observable<Boolean>
        fun showInfo() : Observable<List<ContentfulUnit>>
    }

    class ViewModel(@NonNull val environment: Environment) : ActivityViewModel<LessonsVM>(environment), Inputs, Outputs{
        private val androidContext = EquinoxApp.applicationContext()
        val inputs : Inputs = this
        val outputs: Outputs = this

        //Inputs
        private val onCreate = PublishSubject.create<Unit>()
        private val retry = PublishSubject.create<Unit>()

        //Outputs
        private val showError = BehaviorSubject.create<Unit>()
        private val loading = BehaviorSubject.create<Unit>()
        private val showInfo = BehaviorSubject.create<Unit>()

        init {
            val createEvent = Observables.combineLatest(onCreate,retry)
                .flatMap { getUnits() }
                .share()
        }

        override fun onCreate() {
            return this.onCreate.onNext(Unit)
        }

        override fun retry() {
            return this.retry.onNext(Unit)
        }

        override fun showError(): Observable<String> = this.showError()

        override fun loading(): Observable<Boolean> = this.loading()

        override fun showInfo(): Observable<List<ContentfulUnit>> = this.showInfo()

        private fun getUnits() : Observable<Result<List<ContentfulUnit>>>
        {
            val single = Single.create<Result<List<ContentfulUnit>>> create@{ single ->
                val eventUnit = environment.contentfulUseCase().getUnits(androidContext)
                    .share()
                val units = eventUnit
                    .filter {
                        !it.isFail()
                    }
                    .map {
                        it.successValue()!!
                    }
                val errorUnit = eventUnit
                    .filter {
                        it.isFail()
                    }
                val eventSections = units.flatMap { environment.contentfulUseCase().getSections()}
                    .share()
                val errorSections = eventSections
                    .filter {
                        it.isFail()
                    }
                val section = eventSections
                    .filter {
                        !it.isFail()
                    }
                    .map {
                        it.successValue()!!
                    }
                Observables.combineLatest(errorUnit,errorSections)
                    .subscribe{
                        single.onSuccess(Result.failure(Exception()))
                    }
                Observables.combineLatest(units,section)
                    .map { parseSections(it.first,it.second) }
            }
            return single.toObservable()
        }

        private fun parseSections(listUnit:List<ContentfulUnit>,listSection:List<ContentfulSection>) : List<ContentfulUnit>
        {
            listUnit.forEach {unit ->
                listSection.forEach {section ->
                    if(unit.idUnit == section.idUnit)
                    {
                        unit.sections.add(section)
                    }
                }
            }
            return listUnit
        }
    }

}