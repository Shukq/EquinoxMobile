package com.quinox.mobile.viewModels

import androidx.annotation.NonNull
import com.quinox.domain.entities.ContentfulClass
import com.quinox.domain.entities.ContentfulSection
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.libs.Environment
import com.quinox.domain.entities.Result
import com.quinox.mobile.extensions.resultErrors
import com.quinox.mobile.extensions.resultValues
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject

interface ClassesVM {
    interface Inputs {
        fun classClicked(lesson: ContentfulClass)
    }
    interface Outputs {
        fun classPicker() : Observable<ContentfulClass>
        fun section() : Observable<ContentfulSection>
        fun classList() : Observable<List<ContentfulClass>>
        fun showError() : Observable<String>
    }
    class ViewModel(@NonNull val environment: Environment) : ActivityViewModel<ClassesVM>(environment), Inputs, Outputs {

        val inputs : Inputs = this
        val outputs : Outputs = this

        //Inputs
        private val classClicked = PublishSubject.create<ContentfulClass>()

        //Outputs
        private val section = BehaviorSubject.create<ContentfulSection>()
        private val classList = BehaviorSubject.create<List<ContentfulClass>>()
        private val showError = BehaviorSubject.create<String>()
        private val classPicker = BehaviorSubject.create<ContentfulClass>()

        init {
            val sectionIntent = intent()
                .filter { it.hasExtra("section") }
                .map { it.getSerializableExtra("section") as ContentfulSection }

            sectionIntent
                .subscribe(this.section)

            val event = sectionIntent
                .flatMap { eventServer(it.idSection) }
                .share()

            event
                .resultValues().subscribe(classList)
            event
                .resultErrors()
                .map { "No se pudo cargar las clases" }
                .subscribe(showError)

            classClicked.subscribe(classPicker)
        }

        override fun classClicked(lesson: ContentfulClass) {
            return this.classClicked.onNext(lesson)
        }


        override fun section(): Observable<ContentfulSection> = section
        override fun classList(): Observable<List<ContentfulClass>> = classList
        override fun showError(): Observable<String> = showError
        override fun classPicker(): Observable<ContentfulClass> = classPicker

        fun eventServer(sectionId:String) : Observable<Result<List<ContentfulClass>>>{
            return environment.contentfulUseCase().getLessons(sectionId,environment.context())
        }

    }
}