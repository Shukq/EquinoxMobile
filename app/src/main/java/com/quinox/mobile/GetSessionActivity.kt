package com.quinox.mobile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.viewModels.GetSessionVM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

@RequiresActivityViewModel(GetSessionVM.ViewModel::class)
class GetSessionActivity : BaseActivity<GetSessionVM.ViewModel>() {
    private val disposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_get_session)

        disposable.add(viewModel.outputs.signedInAction().observeOn(AndroidSchedulers.mainThread()).subscribe{
            val intent = Intent(this,HomeActivity::class.java)
            startActivity(intent)
            finishAffinity()
        })
        disposable.add(viewModel.outputs.signedOutAction().observeOn(AndroidSchedulers.mainThread()).subscribe{
            val intent = Intent(this,MainActivity::class.java)
            startActivity(intent)
            finishAffinity()
        })
        viewModel.inputs.onCreate()

    }
    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }
}
