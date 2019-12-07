package com.quinox.mobile.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.quinox.mobile.R
import com.quinox.mobile.anotations.RequiresFragmentViewModel
import com.quinox.mobile.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.fragment_profile.*
import kotlinx.android.synthetic.main.progress_overlay.*

@RequiresFragmentViewModel(ProfileVMFragment.ViewModel::class)
class ProfileFragment : BaseFragment<ProfileVMFragment.ViewModel>() {

    private val compositeDisposable = CompositeDisposable()
    lateinit var progressOverlay: View

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_profile, container, false)
        progressOverlay = root.findViewById(R.id.progress_overlay)
        progressOverlay.visibility = View.VISIBLE
        progressOverlay.bringToFront()
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.inputs.onCreate()

        compositeDisposable.add(viewModel.outputs.attributesList().observeOn(AndroidSchedulers.mainThread()).subscribe {
            progressOverlay.visibility = View.GONE
            it.forEach {pair ->
                when(pair.first){
                    "birthdate" -> {
                        profile_date.setText(pair.second)
                    }
                    "custom:occupation" -> {
                        profile_occupation.setText(pair.second)
                    }
                    "gender" -> {
                        if(pair.second == "male"){
                            profile_gender.setText(getString(R.string.male_es))
                        }else{
                            profile_gender.setText(getString(R.string.female_es))
                        }
                    }
                    "name" -> {
                        profile_name.setText(pair.second)
                    }
                    "email" -> {
                        profile_email.setText(pair.second)
                    }
                }
            }
        })
    }
}