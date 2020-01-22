package com.quinox.mobile

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.MediaController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.viewModels.LessonDetailVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_lesson_detail.*

@RequiresActivityViewModel(LessonDetailVM.ViewModel::class)
class LessonDetailActivity : BaseActivity<LessonDetailVM.ViewModel>() {
    private val compositeDisposable = CompositeDisposable()
    lateinit var adapterImg: ImageAdapter
    lateinit var imgManager: LinearLayoutManager
    lateinit var imgAlbum: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_detail)
        supportActionBar?.title = "Clase"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        imgAlbum = findViewById(R.id.recyclerViewClassDetail)
        imgManager = LinearLayoutManager(this)
        imgManager.orientation = LinearLayoutManager.HORIZONTAL
        imgManager.reverseLayout = true
        imgAlbum.layoutManager = imgManager
        adapterImg = ImageAdapter(this)
        imgAlbum.adapter = adapterImg

        compositeDisposable.add(viewModel.output.loadInfo().observeOn(AndroidSchedulers.mainThread()).subscribe {
            txt_titleClassDetail.text = it.title
            webViewClassDetail.loadData(it.descripcion,"text/html","UTF-8")
            adapterImg.setAlbum(it.imagenes)
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
