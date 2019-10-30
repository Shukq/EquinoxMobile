package com.quinox.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.extensions.onChange
import com.quinox.mobile.viewModels.ConfirmAccVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_confirm_acc.*
import kotlinx.android.synthetic.main.activity_register2.*


@RequiresActivityViewModel(ConfirmAccVM.ViewModel::class)
class ConfirmAccActivity : BaseActivity<ConfirmAccVM.ViewModel>() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_acc)
        supportActionBar?.title = getString(R.string.confirmTitle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        progressBar_codeConfirm.visibility = View.GONE

        input_codeConfirm.onChange { this.viewModel.inputs.code(it) }

        btn_sendConfirm.setOnClickListener {
            this.viewModel.inputs.acceptButtonPressed()
        }
        txt_resendConfirm.setOnClickListener {
            this.viewModel.inputs.resendButtonPressed()
        }

        compositeDisposable.add(this.viewModel.outputs.acceptButtonEnabled().subscribe {
            btn_sendConfirm.isEnabled = it
        })

        compositeDisposable.add(this.viewModel.outputs.loadingEnabled().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it){
                    btn_sendConfirm.isEnabled = false
                    progressBar_codeConfirm.visibility = View.VISIBLE
                }else{
                    btn_sendConfirm.isEnabled = true
                    progressBar_codeConfirm.visibility = View.GONE
                }
            })
        compositeDisposable.add(this.viewModel.outputs.showError()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showDialog(getString(R.string.Sorry),it)
            })

        compositeDisposable.add(this.viewModel.outputs.resendButtonAction()
            .observeOn(AndroidSchedulers.mainThread()).subscribe{
                if (it)
                {
                    Toast.makeText(this,"Codigo reenviado",Toast.LENGTH_SHORT)
                        .show()
                }
                else
                {
                    Toast.makeText(this,"Codigo no reenviado",Toast.LENGTH_SHORT)
                        .show()
                }
            })

        compositeDisposable.add(this.viewModel.outputs.acceptButtonAction()
            .observeOn(AndroidSchedulers.mainThread()).subscribe{
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
                finishAffinity()
            })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun showDialog(title : String, message : String){
        val builder = AlertDialog.Builder(this)
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton(getString(R.string.Close),null)
        val dialog = builder.create()
        dialog.show()
    }
}
