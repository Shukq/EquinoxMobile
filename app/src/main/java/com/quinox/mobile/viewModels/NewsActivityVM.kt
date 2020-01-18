package com.quinox.mobile.viewModels

import com.quinox.domain.entities.ContentfulNews
import com.quinox.mobile.base.ActivityViewModel
import com.quinox.mobile.libs.Environment
import io.reactivex.Observable
import io.reactivex.annotations.NonNull
import io.reactivex.subjects.BehaviorSubject


interface NewsActivityVM {

    interface inputs{}

    interface outputs {
        fun loadInfo(): Observable<ContentfulNews>
    }

    class ViewModel(@NonNull val environment: Environment) : ActivityViewModel<NewsActivityVM>(environment), inputs, outputs{

        val input:inputs = this
        val outputs: outputs = this

        private val loadInfo = BehaviorSubject.create<ContentfulNews>()

        init{
            val newsIntent = intent()
                .filter { it.hasExtra("noticia") }
                .map { it.getSerializableExtra("noticia") as ContentfulNews}
            newsIntent.subscribe(loadInfo)


        }



        override fun loadInfo(): Observable<ContentfulNews> = this.loadInfo

    }
}