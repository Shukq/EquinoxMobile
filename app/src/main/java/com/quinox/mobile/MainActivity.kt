package com.quinox.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.viewModels.MainVM
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

@RequiresActivityViewModel(MainVM.ViewModel::class)
class MainActivity : BaseActivity<MainVM.ViewModel>() {
    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn_logInMain.setOnClickListener {
            viewModel.input.signInBtnPressed()
        }
        btn_createAccMain.setOnClickListener {
            viewModel.input.signUpBtnPressed()
        }
        disposable.add(viewModel.output.signInNav().observeOn(AndroidSchedulers.mainThread()).subscribe {
            val intent = Intent(this,LogInActivity::class.java)
            startActivity(intent)
        })
        disposable.add(viewModel.output.signUpNav().observeOn(AndroidSchedulers.mainThread()).subscribe {
            val intent = Intent(this,Register1Activity::class.java)
            startActivity(intent)
        })
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
