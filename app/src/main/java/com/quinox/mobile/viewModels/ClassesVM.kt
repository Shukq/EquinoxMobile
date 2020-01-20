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

interface ClassesVM {
    interface Inputs {

    }
    interface Outputs {
        fun section() : Observable<ContentfulSection>
        fun classList() : Observable<List<ContentfulClass>>
        fun showError() : Observable<String>
    }
    class ViewModel(@NonNull val environment: Environment) : ActivityViewModel<ClassesVM>(environment), Inputs, Outputs {

        val inputs : Inputs = this
        val outputs : Outputs = this

        //Outputs
        private val section = BehaviorSubject.create<ContentfulSection>()
        private val classList = BehaviorSubject.create<List<ContentfulClass>>()
        private val showError = BehaviorSubject.create<String>()

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
        }

        override fun section(): Observable<ContentfulSection> = section
        override fun classList(): Observable<List<ContentfulClass>> = classList
        override fun showError(): Observable<String> = showError

        fun eventServer(sectionId:String) : Observable<Result<List<ContentfulClass>>>{
            return environment.contentfulUseCase().getLessons(sectionId,environment.context())
        }

    }
}