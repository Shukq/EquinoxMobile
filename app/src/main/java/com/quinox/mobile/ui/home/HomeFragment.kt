package com.quinox.mobile.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.quinox.mobile.R
import com.quinox.mobile.anotations.RequiresFragmentViewModel
import com.quinox.mobile.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_home.*

@RequiresFragmentViewModel(HomeVMFragment.ViewModel::class)
class HomeFragment : BaseFragment<HomeVMFragment.ViewModel>() {
    private val composite = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel.inputs.onCreate()
        composite.add(viewModel.outputs.loadContent().observeOn(AndroidSchedulers.mainThread()).subscribe{
            webViewHome.settings.javaScriptEnabled = true
            webViewHome.settings.domStorageEnabled = true
            webViewHome.settings.setAppCacheEnabled(true)
            webViewHome.settings.loadsImagesAutomatically = true
            webViewHome.settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
            webViewHome.settings.cacheMode = WebSettings.LOAD_NO_CACHE
            Log.e("html",it)
            webViewHome.loadData(it,"text/html","UTF-8")
        })

        return root
    }

    override fun onDestroy() {
        composite.dispose()
        super.onDestroy()
    }
}