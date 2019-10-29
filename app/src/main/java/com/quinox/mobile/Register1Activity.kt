package com.quinox.mobile

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Adapter
import android.widget.AdapterView
import android.widget.ArrayAdapter
import com.quinox.domain.entities.Gender
import com.quinox.mobile.anotations.RequiresActivityViewModel
import com.quinox.mobile.base.BaseActivity
import com.quinox.mobile.extensions.onChange
import com.quinox.mobile.utils.DatePickerFragment
import com.quinox.mobile.viewModels.Register1VM
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_register1.*
@RequiresActivityViewModel(Register1VM.ViewModel::class)
class Register1Activity : BaseActivity<Register1VM.ViewModel>() {
    private val disposable = CompositeDisposable()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register1)
        supportActionBar?.title = getString(R.string.registerTitle)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.setDisplayShowHomeEnabled(true)
        btn_nextregister.visibility = View.GONE
        val staticAdapter = ArrayAdapter
            .createFromResource(
                this, R.array.spin_Gender,
                R.layout.spinner_item
            )

        // Specify the layout to use when the list of choices appears
        staticAdapter
            .setDropDownViewResource(R.layout.spinner_dropdown_item)

        // Apply the adapter to the spinner
        spin_generoRegister.adapter = staticAdapter
        spin_generoRegister.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                when (spin_generoRegister.selectedItem){
                    getString(R.string.male) -> viewModel.inputs.gender(Gender.male)
                    else -> viewModel.inputs.gender(Gender.female)
                }
            }
        }

            datePicker.setOnClickListener {
            viewModel.inputs.datePressed()
        }
        input_nameRegister.onChange {
            viewModel.inputs.username(it)
        }
        input_emailRegister.onChange {
            viewModel.inputs.email(it)
        }
        input_workRegister.onChange {
            viewModel.inputs.occupation(it)
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
            val selectedDate = twoDigits(dayOfMonth) + "/" + (twoDigits(month + 1)) + "/" + twoDigits(year)
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
