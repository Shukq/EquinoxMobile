package com.quinox.mobile

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.viewModels.ClassesVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable


@RequiresActivityViewModel(ClassesVM.ViewModel::class)
class LessonsActivity : BaseActivity<ClassesVM.ViewModel>() {
    private val compositeDisposable = CompositeDisposable()
    lateinit var recycler: RecyclerView
    lateinit var adapter: lessonsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lessons)
        supportActionBar?.title = "Clases"
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        recycler = findViewById(R.id.recycler_Lessons)
        adapter = lessonsAdapter {
            viewModel.inputs.classClicked(it)
        }
        compositeDisposable.add(viewModel.outputs.section().observeOn(AndroidSchedulers.mainThread()).subscribe {
            supportActionBar?.title = it.title
        })
        compositeDisposable.add(viewModel.outputs.classPicker().observeOn(AndroidSchedulers.mainThread()).subscribe {
            val intent = Intent(this,LessonDetailActivity::class.java)
            intent.putExtra("clase",it)
            startActivity(intent)
        })
        compositeDisposable.add(viewModel.outputs.classList().observeOn(AndroidSchedulers.mainThread()).subscribe{
            adapter.setLessonList(it.reversed())
        })
        recycler.adapter = adapter

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
