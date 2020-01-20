package com.quinox.domain.useCases

import android.content.Context
import com.quinox.domain.entities.*
import io.reactivex.Observable

interface ContentfulUseCase {

    fun getHomePage() : Observable<Result<String>>
    fun getUnits(context:Context) : Observable<Result<List<ContentfulUnit>>>
    fun getSections() : Observable<Result<List<ContentfulSection>>>
    fun getLessons(sectionId:String, context:Context) : Observable<Result<List<ContentfulClass>>>
    fun getNews() : Observable<Result<List<ContentfulNews>>>
}