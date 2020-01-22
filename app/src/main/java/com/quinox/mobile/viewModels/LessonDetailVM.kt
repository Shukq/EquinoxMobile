package com.quinox.mobile.viewModels


import com.quinox.domain.entities.ContentfulClass
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.libs.Environment
import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.subjects.BehaviorSubject

interface LessonDetailVM {

    interface inputs{}

    interface outputs {
        fun loadInfo(): Observable<ContentfulClass>
    }

    class ViewModel(@NonNull val environment: Environment) : ActivityViewModel<LessonDetailVM>(environment),inputs,outputs{
        val input: inputs =  this
        val output: outputs = this

        private val loadInfo = BehaviorSubject.create<ContentfulClass>()

        init{
            val classIntent = intent()
                .filter { it.hasExtra("clase") }
                .map { it.getSerializableExtra("clase")  as ContentfulClass }
            classIntent.subscribe(loadInfo)
        }

        override fun loadInfo(): Observable<ContentfulClass> = this.loadInfo
    }

}