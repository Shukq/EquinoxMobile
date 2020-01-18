package com.quinox.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.viewModels.NewsActivityVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_log_in.view.*
import kotlinx.android.synthetic.main.activity_news.*

@RequiresActivityViewModel(NewsActivityVM.ViewModel::class)
class NewsActivity : BaseActivity<NewsActivityVM.ViewModel>(){
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)
        supportActionBar?.title = "Noticia"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        compositeDisposable.add(viewModel.outputs.loadInfo().observeOn(AndroidSchedulers.mainThread()).subscribe{
            txt_titleNews.text = it.title
            txt_headerNews.text = it.header
            wv_descripNews.loadData(it.description,"text/html","UTF-8")
        })

    }


    override fun onDestroy() {
        compositeDisposable.dispose()
        super.onDestroy()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
