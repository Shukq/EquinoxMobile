package com.quinox.awsplatform.useCases

import android.content.Context
import android.util.Log
import android.webkit.WebSettings
import com.contentful.java.cda.CDAArray
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
import org.jsoup.Jsoup

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

    override fun getLessons(sectionId: String,context: Context): Observable<Result<List<ContentfulClass>>> {
        Log.e("ENTRO","SI")
        val listClass = mutableListOf<ContentfulClass>()
        val single = Single.create<Result<List<ContentfulClass>>> create@{ single ->
            val lesson = client.observe(CDAEntry::class.java)
                .where("content_type","cursoV1")
                .linksToEntryId(sectionId)
                .all()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .toObservable()
            lesson.subscribe {
                for (resource in it.items()){
                    val entry = resource as CDAEntry
                    val id: String = entry.id()
                    val title: String = entry.getField("titulo")
                    val richText: CDARichDocument = entry.getField("descripcion")
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
                    val assets : List<CDAAsset>? = entry.getField("media")
                    val listAsset : MutableList<String> = mutableListOf()
                    assets?.forEach {
                        listAsset.add(it.url())
                        Log.e("CLASE-url",it.url())
                    }

                    val richTextVideo: CDARichDocument? = entry.getField("urlVideo")
                    processor.overrideRenderer(
                        { _, node -> node is CDARichEmbeddedBlock && node.data is CDAAsset },
                        { processor1, node ->
                            val block = node as CDARichEmbeddedBlock
                            val data = block.data as CDAAsset
                            return@overrideRenderer "<img src=http:${data.url()} style=\"width: 100%\" />"

                        }
                    )
                    if(richTextVideo != null)
                    {
                        val htmlVideo = processor.process(htmlContext,richTextVideo)
                        val doc = Jsoup.parse(htmlVideo)
                        val urlVideo = doc.select("div a")
                        val lessonObj = ContentfulClass(id,title,html,listAsset,urlVideo.attr("href"))
                        Log.e("CLASE-id",lessonObj.id)
                        Log.e("CLASE-title",lessonObj.title)
                        Log.e("CLASE-desc",lessonObj.descripcion)
                        Log.e("CLASE-video",urlVideo.attr("href"))
                    }
                    else
                    {
                        val lessonObj = ContentfulClass(id,title,html,listAsset,null)
                        Log.e("CLASE-id",lessonObj.id)
                        Log.e("CLASE-title",lessonObj.title)
                        Log.e("CLASE-desc",lessonObj.descripcion)
                    }


                }
            }

        }
        return single.toObservable()
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