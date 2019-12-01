package com.quinox.awsplatform.useCases

import android.util.Log
import android.webkit.WebSettings
import com.contentful.java.cda.CDAAsset
import com.contentful.java.cda.CDAEntry
import com.contentful.java.cda.rich.CDARichDocument
import com.contentful.java.cda.rich.CDARichEmbeddedBlock
import com.contentful.rich.html.HtmlContext
import com.contentful.rich.html.HtmlProcessor
import com.quinox.awsplatform.useCases.Contentful.Client
import com.quinox.domain.entities.Result
import com.quinox.domain.useCases.ContentfulUseCase
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class ContentfulUseCase:ContentfulUseCase{
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
            homePage.doOnError {
                single.onSuccess(Result.failure(Exception()))
            }
        }
        return single.toObservable()
    }

}