package com.quinox.domain.useCases

import com.quinox.domain.entities.ContentfulClass
import com.quinox.domain.entities.ContentfulSection
import com.quinox.domain.entities.ContentfulUnit
import com.quinox.domain.entities.Result
import io.reactivex.Observable

interface ContentfulUseCase {

    fun getHomePage() : Observable<Result<String>>
    fun getUnits() : Observable<Result<List<ContentfulUnit>>>
    fun getSections(unitId:String) : Observable<Result<List<ContentfulSection>>>
    fun getLessons(sectionId:String) : Observable<Result<List<ContentfulClass>>>
    fun getNews() : Observable<Result<String>>
}