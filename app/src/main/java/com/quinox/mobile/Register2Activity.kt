package com.quinox.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.extensions.onChange
import com.quinox.mobile.viewModels.Register2VM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_register2.*

@RequiresActivityViewModel(Register2VM.ViewModel::class)
class Register2Activity : BaseActivity<Register2VM.ViewModel>() {
    private val compositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register2)
        this.supportActionBar?.title = getString(R.string.registerTitle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        progressBar_Register2.visibility = View.GONE

        passTxtTempPass.onChange { this.viewModel.inputs.password(it) }

        create_account_btn.setOnClickListener {
            this.viewModel.inputs.signUpButtonPressed()
        }

        compositeDisposable.add(this.viewModel.outputs.signUpButtonEnabled().subscribe {
            create_account_btn.isEnabled = it
        })

        compositeDisposable.add(this.viewModel.outputs.validPasswordCapitalLowerLetters().subscribe {
            this.passOption(img_error1Register2,it)
        })
        compositeDisposable.add(this.viewModel.outputs.validPasswordNumbers().subscribe{
            this.passOption(img_error2Register2,it)
        })
        compositeDisposable.add(this.viewModel.outputs.validPasswordSpecialCharacters().subscribe {
            this.passOption(img_error3Register2,it)
        })
        compositeDisposable.add(this.viewModel.outputs.validPasswordLenght().subscribe {
            this.passOption(img_error4Register2,it)
        })

        compositeDisposable.add(this.viewModel.outputs.loadingEnabled().observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                if (it){
                    create_account_btn.isEnabled = false
                    progressBar_Register2.visibility = View.VISIBLE
                }else{
                    create_account_btn.isEnabled = true
                    progressBar_Register2.visibility = View.GONE
                }
            })
        compositeDisposable.add(this.viewModel.outputs.showError()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                showDialog(getString(R.string.Sorry),it)
            })

        compositeDisposable.add(this.viewModel.outputs.confirmAccAction()
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                val intent = Intent(this,ConfirmAccActivity::class.java)
                intent.putExtra("SignUpModel",it)
                startActivity(intent)
            })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    override fun onBackPressed() {
        finish()
    }

    private fun passOption(image : ImageView, state: Boolean){
        if(state){
            image.setImageResource(R.drawable.icon_ok)
        }else{
            image.setImageResource(R.drawable.icon_close)
        }
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
        compositeDisposable.dispose()
        super.onDestroy()
    }
}
