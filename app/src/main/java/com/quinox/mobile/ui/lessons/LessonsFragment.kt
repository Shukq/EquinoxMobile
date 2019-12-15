package com.quinox.mobile.ui.lessons

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.quinox.mobile.LessonsActivity
import com.quinox.mobile.R
import com.quinox.mobile.anotations.RequiresFragmentViewModel
import com.quinox.mobile.base.BaseFragment
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable

@RequiresFragmentViewModel(LessonsVM.ViewModel::class)
class LessonsFragment : BaseFragment<LessonsVM.ViewModel>() {
    private val composite = CompositeDisposable()
    lateinit var recycler: RecyclerView
    lateinit var unitAdapter: UnitAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_lessons, container, false)
        recycler = root.findViewById(R.id.recycler_Units)
        unitAdapter = UnitAdapter{
            viewModel.inputs.openSectionPicker(it)
        }
        recycler.adapter = unitAdapter
        viewModel.inputs.onCreate()
        composite.add(viewModel.outputs.showInfo().observeOn(AndroidSchedulers.mainThread()).subscribe{
            unitAdapter.setUnitList(it)
        })

        composite.add(viewModel.outputs.sectionPicker().observeOn(AndroidSchedulers.mainThread()).subscribe {
            val sections = it
            var selectedSection = sections[0]
            val listToShow : List<CharSequence> = sections.map { section -> section.title }
            val d = MaterialAlertDialogBuilder(activity, R.style.ThemeOverlay_MaterialComponents_MaterialAlertDialog_Centered)
            d.setTitle("Seleccione una sección")
            d.setSingleChoiceItems(listToShow.toTypedArray(), 0) { _ , i ->
                selectedSection = sections[i]
            }
            d.setPositiveButton("Seleccionar"){dialogInterface, _ ->
                this.viewModel.inputs.selectedSection(selectedSection)
                dialogInterface.cancel()
            }
            d.setNegativeButton(getString(R.string.Close)){dialogInterface, _ ->
                dialogInterface.cancel()
            }
            d.show()
        })

        composite.add(viewModel.outputs.selectedSectionAction().observeOn(AndroidSchedulers.mainThread()).subscribe {
            val intent = Intent(activity, LessonsActivity::class.java)
            intent.putExtra("section",it)
            startActivity(intent)
        })

        return root
    }

    override fun onDestroy() {
        composite.dispose()
        super.onDestroy()
    }

}