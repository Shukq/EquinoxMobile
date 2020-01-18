package com.quinox.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import androidx.appcompat.app.AlertDialog
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.extensions.onChange
import com.quinox.mobile.viewModels.ForgotPasswordVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

@RequiresActivityViewModel(ForgotPasswordVM.ViewModel::class)
class ForgotPasswordActivity : BaseActivity<ForgotPasswordVM.ViewModel>() {

    private val disposable = CompositeDisposable()
    lateinit var progressBar: ProgressBar
    lateinit var nextBtn : Button
    lateinit var input : EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        supportActionBar?.title = getString(R.string.Forgot_password)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        progressBar = findViewById(R.id.progressBar4)
        progressBar.visibility = View.GONE
        nextBtn = findViewById(R.id.forgot_next_btn)
        input = findViewById(R.id.forgot_username)
        input.onChange { this.viewModel.inputs.usernameChanged(it) }

        nextBtn.setOnClickListener { this.viewModel.inputs.nextButtonPressed() }
        disposable.add(this.viewModel.outputs.nextButtonEnabled().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                nextBtn.isEnabled = it
            })

        disposable.add(this.viewModel.outputs.loadingEnabled().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it){
                    nextBtn.isEnabled = false
                    progressBar.visibility = View.VISIBLE
                }else{
                    nextBtn.isEnabled = true
                    progressBar.visibility = View.GONE
                }
            })

        disposable.add(this.viewModel.outputs.showError().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showDialog(getString(R.string.Sorry),it)
            })

        disposable.add(this.viewModel.outputs.forgotPasswordStatus().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val intent = Intent(this, ConfirmForgotPasswordActivity::class.java)
                intent.putExtra("username",it)
                startActivity(intent)
                finish()
            })
    }

    private fun showDialog(title : String, message : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.Close),null)
        val dialog = builder.create()
        dialog.show()
    }

    override fun onDestroy() {
        disposable.dispose()
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
