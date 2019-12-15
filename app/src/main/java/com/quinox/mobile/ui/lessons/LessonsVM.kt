package com.quinox.mobile.ui.lessons

import androidx.annotation.NonNull
import com.quinox.domain.entities.ContentfulSection
import com.quinox.domain.entities.ContentfulUnit
import com.quinox.mobile.libs.Environment
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.rxkotlin.Observables
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import com.quinox.domain.entities.Result
import com.quinox.mobile.base.FragmentViewModel
import java.lang.Exception

interface LessonsVM {
    interface Inputs {
        fun onCreate()
        fun retry()
        fun openSectionPicker(sections: List<ContentfulSection>)
        fun selectedSection(section: ContentfulSection)
    }
    interface Outputs {
        fun showError() : Observable<String>
        fun loading() : Observable<Boolean>
        fun showInfo() : Observable<List<ContentfulUnit>>
        fun sectionPicker() : Observable<List<ContentfulSection>>
        fun selectedSectionAction() : Observable<ContentfulSection>
    }

    class ViewModel(@NonNull val environment: Environment) : FragmentViewModel<LessonsVM>(environment), Inputs, Outputs{

        val inputs : Inputs = this
        val outputs: Outputs = this

        //Inputs
        private val onCreate = PublishSubject.create<Unit>()
        private val retry = PublishSubject.create<Unit>()
        private val openSectionPicker = PublishSubject.create<List<ContentfulSection>>()
        private val selectedSection = PublishSubject.create<ContentfulSection>()

        //Outputs
        private val showError = BehaviorSubject.create<String>()
        private val loading = BehaviorSubject.create<Boolean>()
        private val showInfo = BehaviorSubject.create<List<ContentfulUnit>>()
        private val sectionPicker = BehaviorSubject.create<List<ContentfulSection>>()
        private val selectedSectionAction = BehaviorSubject.create<ContentfulSection>()

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

            openSectionPicker
                .subscribe(sectionPicker)

            selectedSection
                .subscribe(selectedSectionAction)
        }

        override fun onCreate() {
            return this.onCreate.onNext(Unit)
        }

        override fun retry() {
            return this.retry.onNext(Unit)
        }

        override fun openSectionPicker(sections: List<ContentfulSection>) {
            return this.openSectionPicker.onNext(sections)
        }

        override fun selectedSection(section: ContentfulSection) {
            return this.selectedSection.onNext(section)
        }

        override fun selectedSectionAction(): Observable<ContentfulSection> = this.selectedSectionAction

        override fun sectionPicker(): Observable<List<ContentfulSection>> = this.sectionPicker

        override fun showError(): Observable<String> = this.showError

        override fun loading(): Observable<Boolean> = this.loading

        override fun showInfo(): Observable<List<ContentfulUnit>> = this.showInfo

        private fun eventServer() : Observable<Result<List<ContentfulUnit>>>
        {
            val single = Single.create<Result<List<ContentfulUnit>>> create@{ single ->
                val eventUnit = environment.contentfulUseCase().getUnits(environment.context())
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
                    .subscribe { single.onSuccess(Result.success(it)) }
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