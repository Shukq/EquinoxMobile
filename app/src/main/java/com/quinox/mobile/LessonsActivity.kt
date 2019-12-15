package com.quinox.mobile

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.viewModels.LessonsVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_lessons.*


@RequiresActivityViewModel(LessonsVM.ViewModel::class)
class LessonsActivity : BaseActivity<LessonsVM.ViewModel>() {
    private val compositeDisposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lessons)
        supportActionBar?.title = "Clases"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        compositeDisposable.add(viewModel.outputs.section().observeOn(AndroidSchedulers.mainThread()).subscribe {
            //lessons_title.text = it.title
            supportActionBar?.title = it.title
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
