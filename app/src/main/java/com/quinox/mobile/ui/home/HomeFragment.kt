package com.quinox.mobile.ui.home

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.quinox.mobile.R
import com.quinox.mobile.anotations.RequiresFragmentViewModel
import com.quinox.mobile.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.nav_header_home.*

@RequiresFragmentViewModel(HomeVMFragment.ViewModel::class)
class HomeFragment : BaseFragment<HomeVMFragment.ViewModel>() {
    private val composite = CompositeDisposable()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_home, container, false)
        viewModel.inputs.onCreate()
        composite.add(viewModel.outputs.loadContent().observeOn(AndroidSchedulers.mainThread()).subscribe{
            Log.e("html",it)
            webViewHome.loadData(it,"text/html","UTF-8")
        })
        composite.add(viewModel.outputs.showError().observeOn(AndroidSchedulers.mainThread()).subscribe {

            //showError("Sin conexion","Porfavor conecte su dispositivo a internet")
            Snackbar.make(root,R.string.RetryText,Snackbar.LENGTH_INDEFINITE)
                .setAction(R.string.Retry) { viewModel.inputs.onCreate() }
                .setActionTextColor(resources.getColor(R.color.secondaryLightColor,null))
                .show()
        })


        return root
    }

    override fun onDestroy() {
        composite.dispose()
        super.onDestroy()
    }

    private fun showError(title : String, message: String){
        class MyDialogFragment2 : androidx.fragment.app.DialogFragment() {
            override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
                return AlertDialog.Builder(activity!!)
                    .setTitle(title)
                    .setMessage(message)
                    .setPositiveButton(getString(R.string.Retry)) {_,_ ->
                        viewModel.inputs.onCreate()
                    }
                    .setNegativeButton(getString(R.string.Close),null)
                    .create()
            }
        }
        MyDialogFragment2().show(fragmentManager!!,"showError")
    }
}