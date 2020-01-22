package com.quinox.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.viewModels.LessonDetailVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

@RequiresActivityViewModel(LessonDetailVM.ViewModel::class)
class LessonDetailActivity : BaseActivity<LessonDetailVM.ViewModel>() {
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lesson_detail)
        supportActionBar?.title = "Clase"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        compositeDisposable.add(viewModel.output.loadInfo().observeOn(AndroidSchedulers.mainThread()).subscribe {

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
