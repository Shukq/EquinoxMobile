package com.quinox.awsplatform.useCases

import android.content.Context
import android.util.Log
import android.webkit.WebSettings
import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.rich.CDARichDocument
import com.contentful.java.cda.rich.CDARichEmbeddedBlock
import com.contentful.rich.android.AndroidContext
import com.contentful.rich.android.AndroidProcessor
import com.contentful.rich.html.HtmlContext
import com.contentful.rich.html.HtmlProcessor
import com.quinox.awsplatform.useCases.Contentful.Client
import com.quinox.domain.entities.*
import com.quinox.domain.useCases.ContentfulUseCase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.plugins.RxJavaPlugins
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class ContentfulUseCase:ContentfulUseCase{
    override fun getUnits(context:Context): Observable<Result<List<ContentfulUnit>>> {
        val listUnit = mutableListOf<ContentfulUnit>()
        val single = Single.create<Result<List<ContentfulUnit>>> create@{ single ->
            val unidad = client.observe(CDAEntry::class.java)
                .where("content_type", "unidad")
                .all()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .toObservable()
            unidad.subscribe {
                for (resource in it.items()) {
                    val entry = resource as CDAEntry
                    val id: String = entry.id()
                    val title: String = entry.getField("numero")
                    val richText: CDARichDocument = entry.getField("descripcion")
                    val sequenceProcessor = AndroidProcessor.creatingCharSequences()
                    val contextAndroid = AndroidContext(context)
                    val result = sequenceProcessor.process(contextAndroid,richText)
                    val unit = ContentfulUnit(id,title,result.toString())
                    listUnit.add(unit)
                }
                single.onSuccess(Result.success(listUnit))
            }
            unidad
                .subscribeBy (
                    onError = {
                        single.onSuccess(Result.failure(Exception()))
                    }
                )
        }
        return single.toObservable()
    }

    override fun getSections(): Observable<Result<List<ContentfulSection>>> {
        val listSection = mutableListOf<ContentfulSection>()
        val single = Single.create<Result<List<ContentfulSection>>> create@{ single ->
            val seccion = client.observe(CDAEntry::class.java)
                .where("content_type", "secciones")
                .all()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .toObservable()
            seccion.subscribe {
                for (resource in it.items()) {
                    val entry = resource as CDAEntry
                    val id: String = entry.id()
                    val title: String = entry.getField("numeroDeSeccion")
                    val idUnit: CDAEntry = entry.getField("unidad")
                    val section = ContentfulSection(id,title,idUnit.id())
                    listSection.add(section)
                }
                single.onSuccess(Result.success(listSection))
            }
            seccion
                .subscribeBy (
                    onError = {
                        single.onSuccess(Result.failure(Exception()))
                    }
                )
        }
        return single.toObservable()
    }

    override fun getLessons(sectionId: String): Observable<Result<List<ContentfulClass>>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getNews(): Observable<Result<List<ContentfulNews>>> {
        val listNews = mutableListOf<ContentfulNews>()
        val single = Single.create<Result<List<ContentfulNews>>> create@{ single ->
            val noticias = client.observe(CDAEntry::class.java)
                .where("content_type","noticias")
                .all()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .toObservable()
            noticias.subscribe {
                for (resource in it.items()) {
                    val entry = resource as CDAEntry
                    val id: String = entry.id()
                    val title: String = entry.getField("titulo")
                    val header: String = entry.getField("cabecera")
                    val richText: CDARichDocument = entry.getField("descripcion")
                    Log.e("titulo",title)
                    val htmlContext = HtmlContext()
                    val processor = HtmlProcessor()
                    processor.overrideRenderer(
                        { _, node -> node is CDARichEmbeddedBlock && node.data is CDAAsset },
                        { processor1, node ->
                            val block = node as CDARichEmbeddedBlock
                            val data = block.data as CDAAsset
                            return@overrideRenderer "<img src=http:${data.url()} style=\"width: 100%\" />"

                        }
                    )
                    val html = processor.process(htmlContext,richText)
                    val news = ContentfulNews(id,title,header,html)
                    listNews.add(news)
                }
                single.onSuccess(Result.success(listNews))
            }
            noticias
                .subscribeBy (
                    onError = {
                        single.onSuccess(Result.failure(Exception()))
                    }
                )
        }
        return single.toObservable()
    }

    private val client = Client.getContentful()
    override fun getHomePage(): Observable<Result<String>> {
        val single = Single.create<Result<String>> create@{ single ->
            val homePage = client.observe(CDAEntry::class.java)
                .one("57JtssG1UUpRmHmZ8ze5GF")
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .toObservable()
            homePage.subscribe{
                val richText : CDARichDocument = it.getField("mainText")
                val htmlContext = HtmlContext()
                val processor = HtmlProcessor()
                processor.overrideRenderer(
                    { _, node -> node is CDARichEmbeddedBlock && node.data is CDAAsset },
                    { processor1, node ->
                        val block = node as CDARichEmbeddedBlock
                        val data = block.data as CDAAsset
                        return@overrideRenderer "<img src=http:${data.url()} style=\"width: 100%\" />"

                    }
                )
                val html = processor.process(htmlContext,richText)
                single.onSuccess(Result.success(html))
            }
            homePage
                .subscribeBy (
                    onError = {
                        single.onSuccess(Result.failure(Exception()))
                    }
                )
        }
        return single.toObservable()
    }

}