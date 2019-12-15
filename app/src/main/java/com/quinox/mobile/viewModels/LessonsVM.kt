package com.quinox.mobile.viewModels

import androidx.annotation.NonNull
import com.quinox.domain.entities.ContentfulSection
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.libs.Environment
import io.reactivex.Observable
import io.reactivex.subjects.BehaviorSubject

interface LessonsVM {
    interface Inputs {

    }
    interface Outputs {
        fun section() : Observable<ContentfulSection>
    }
    class ViewModel(@NonNull val environment: Environment) : ActivityViewModel<LessonsVM>(environment), Inputs, Outputs {

        val inputs : Inputs = this
        val outputs : Outputs = this

        //Outputs
        private val section = BehaviorSubject.create<ContentfulSection>()

        init {
            val sectionIntent = intent()
                .filter { it.hasExtra("section") }
                .map { it.getSerializableExtra("section") as ContentfulSection }

            sectionIntent
                .subscribe(this.section)
        }

        override fun section(): Observable<ContentfulSection> = section

    }
}