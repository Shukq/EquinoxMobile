package com.quinox.mobile.ui.news

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.quinox.mobile.NewsActivity
import com.quinox.mobile.R
import com.quinox.mobile.anotations.RequiresFragmentViewModel
import com.quinox.mobile.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


@RequiresFragmentViewModel(NewsVM.ViewModel::class)
class NewsFragment : BaseFragment<NewsVM.ViewModel>() {
    private val composite = CompositeDisposable()
    lateinit var recycler: RecyclerView
    lateinit var newsAdapter: NewsAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_news, container, false)
        recycler = root.findViewById(R.id.recycler_News)
        newsAdapter = NewsAdapter{
            this.viewModel.inputs.newsClicked(it)
        }
        recycler.adapter = newsAdapter

        composite.add(viewModel.outputs.showInfo().observeOn(AndroidSchedulers.mainThread()).subscribe {
            newsAdapter.setNewsList(it)
            Log.e("NewsList", it.size.toString())
        })

        composite.add(viewModel.outputs.goToNews().observeOn(AndroidSchedulers.mainThread()).subscribe{
            val intent = Intent(activity, NewsActivity::class.java)
            intent.putExtra("noticia",it)
            startActivity(intent)
        })

        viewModel.inputs.onCreate()
        return root
    }
    override fun onDestroy() {
        composite.dispose()
        super.onDestroy()
    }
}