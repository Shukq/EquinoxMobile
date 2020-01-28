package com.quinox.mobile

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import com.quinox.domain.entities.Gender
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.extensions.onChange
import com.quinox.mobile.utils.DatePickerFragment
import com.quinox.mobile.viewModels.Register1VM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_confirm_forgot_password.*
import kotlinx.android.synthetic.main.activity_register1.*

@RequiresActivityViewModel(Register1VM.ViewModel::class)
class Register1Activity : BaseActivity<Register1VM.ViewModel>() {
    private val disposable = CompositeDisposable()
    lateinit var genDropDown : AutoCompleteTextView
    lateinit var ocuDropDown : AutoCompleteTextView
    private var listGen : List<String> = listOf()
    private var listOcu : List<String> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register1)
        supportActionBar?.title = getString(R.string.registerTitle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        genDropDown = findViewById(R.id.input_genRegister)
        ocuDropDown = findViewById(R.id.input_ocuRegister)
        btn_nextregister.visibility = View.GONE


        listGen = resources.getStringArray(R.array.spin_Gender).asList()
        listOcu = resources.getStringArray(R.array.spin_Ocupation).asList()

        var adapterGen = ArrayAdapter(this,R.layout.spinner_dropdown_item,listGen)
        var adapterOcu = ArrayAdapter(this,R.layout.spinner_dropdown_item,listOcu)

        genDropDown.setAdapter(adapterGen)
        ocuDropDown.setAdapter(adapterOcu)






        datePicker.setOnClickListener {
            viewModel.inputs.datePressed()
        }
        input_nameRegister.onChange {
            viewModel.inputs.username(it)
        }
        input_emailRegister.onChange {
            viewModel.inputs.email(it)
        }

        input_genRegister.onChange {
            when (it) {
                getString(R.string.male) -> viewModel.inputs.gender(Gender.male)
                getString(R.string.female) -> viewModel.inputs.gender(Gender.female)
                getString(R.string.other) -> viewModel.inputs.gender(Gender.other)
            }
        }

        input_ocuRegister.onChange {
            when (it) {
                getString(R.string.student) -> viewModel.inputs.occupation("Estudiante")
                getString(R.string.admin) -> viewModel.inputs.occupation("Administrativo")
                getString(R.string.teacher) -> viewModel.inputs.occupation("Docente")
                getString(R.string.otherOcu) -> viewModel.inputs.occupation("Otro")
            }
        }

        btn_nextregister.setOnClickListener {
            viewModel.inputs.nextButtonPressed()
        }
        disposable.add(viewModel.outputs.dateTextChanged().observeOn(AndroidSchedulers.mainThread()).subscribe{
            lbl_date.text = it
        })
        disposable.add(viewModel.outputs.nextButtonIsEnabled().observeOn(AndroidSchedulers.mainThread()).subscribe{
            if (it)
            {
                btn_nextregister.visibility = View.VISIBLE
            }
            else
            {
                btn_nextregister.visibility = View.GONE
            }
        })
        disposable.add(viewModel.outputs.openDatePicker().observeOn(AndroidSchedulers.mainThread()).subscribe{
            showDatePickerDialog()
        })
        disposable.add(viewModel.outputs.register1Action().observeOn(AndroidSchedulers.mainThread()).subscribe{
            val intent = Intent(this,Register2Activity::class.java)
            intent.putExtra("model",it)
            startActivity(intent)
            finish()
        })
    }





    override fun onBackPressed() {
        super.onBackPressed()
        finish()
    }

    fun showDatePickerDialog(){
        val listener: DatePickerDialog.OnDateSetListener = DatePickerDialog.OnDateSetListener {
                view, year, month, dayOfMonth ->
            val selectedDate =  twoDigits(year) + "-" + (twoDigits(month + 1)) + "-" + twoDigits(dayOfMonth)
            Log.e("Date", "selected  $selectedDate  ")
            viewModel.inputs.dateActionReceive(selectedDate)

        }
        val newFragment: DatePickerFragment = DatePickerFragment.newInstance(listener)
        newFragment.show(supportFragmentManager,"datePicker")
    }
    fun twoDigits(n: Int): String {
        return if (n <= 9) "0$n" else n.toString()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
    override fun onDestroy() {
        disposable.dispose()
        super.onDestroy()
    }


}
