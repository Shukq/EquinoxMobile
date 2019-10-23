package com.quinox.mobile

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jakewharton.rxbinding2.view.enabled
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.extensions.onChange
import com.quinox.mobile.viewModels.SignInVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_log_in.*


@RequiresActivityViewModel(SignInVM.ViewModel::class)
class LogInActivity : BaseActivity<SignInVM.ViewModel>() {
    private val disposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_in)

        progressBar_LogIn.visibility = View.GONE

        btn_logLogIn.setOnClickListener {
            viewModel.inputs.signInButtonPressed()
        }
        input_userLogIn.onChange {
            viewModel.inputs.username(it)
        }
        input_passLogIn.onChange {
            viewModel.inputs.password(it)
        }

        disposable.add(viewModel.outputs.signInButtonIsEnabled().observeOn(AndroidSchedulers.mainThread()).subscribe {
            btn_logLogIn.isEnabled = it
        })
        disposable.add(viewModel.outputs.loadingEnabled().observeOn(AndroidSchedulers.mainThread()).subscribe {
            btn_logLogIn.isEnabled = !it
            if(it)
            {
                progressBar_LogIn.visibility = View.VISIBLE
            }
            else
            {
                progressBar_LogIn.visibility = View.GONE
            }
        })
        disposable.add(viewModel.outputs.showError().observeOn(AndroidSchedulers.mainThread()).subscribe {
            Toast.makeText(this,it,Toast.LENGTH_SHORT).show()
        })
        disposable.add(viewModel.outputs.signedInAction().observeOn(AndroidSchedulers.mainThread()).subscribe {
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finish()
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }

    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
