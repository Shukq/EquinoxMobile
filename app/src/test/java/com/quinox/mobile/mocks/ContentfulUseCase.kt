package com.quinox.mobile.mocks

import android.content.Context
import com.quinox.domain.entities.ContentfulClass
import com.quinox.domain.entities.ContentfulSection
import com.quinox.domain.entities.ContentfulUnit
import com.quinox.domain.entities.Result
import com.quinox.domain.useCases.ContentfulUseCase
import io.reactivex.Observable
import io.reactivex.Single

class ContentfulUseCase : ContentfulUseCase {
    override fun getHomePage(): Observable<Result<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getUnits(context: Context): Observable<Result<List<ContentfulUnit>>> {
        val single = Single.create<Result<List<ContentfulUnit>>> { single ->
            val listUnit = mutableListOf<ContentfulUnit>()
            val unit1 = ContentfulUnit("id1","title1","desc1")
            val unit2 = ContentfulUnit("id2","title2","desc2")
            val unit3 = ContentfulUnit("id3","title3","desc3")
            listUnit.add(unit1)
            listUnit.add(unit2)
            listUnit.add(unit3)
            single.onSuccess(Result.success(listUnit))
        }
        return single.toObservable()
    }

    override fun getSections(): Observable<Result<List<ContentfulSection>>> {
        val single = Single.create<Result<List<ContentfulSection>>> { single ->
            val listSection = mutableListOf<ContentfulSection>()
            val section1 = ContentfulSection("id1","title1","id1")
            val section2 = ContentfulSection("id2","title2","id1")
            val section3 = ContentfulSection("id3","title3","id3")
            listSection.add(section1)
            listSection.add(section2)
            listSection.add(section3)
            single.onSuccess(Result.success(listSection))
        }
        return single.toObservable()
    }

    override fun getLessons(sectionId: String): Observable<Result<List<ContentfulClass>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNews(): Observable<Result<String>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}