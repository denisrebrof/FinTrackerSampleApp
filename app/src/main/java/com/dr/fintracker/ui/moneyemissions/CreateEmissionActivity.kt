package com.dr.fintracker.ui.moneyemissions

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dr.fintracker.R
import com.dr.fintracker.data.entities.EmissionData
import com.dr.fintracker.data.entities.EmissionDirection
import com.dr.fintracker.data.entities.EmissionRepeatRule
import com.dr.fintracker.data.entities.MoneyEmission
import com.dr.fintracker.ui.MainActivity
import com.dr.fintracker.viewmodel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_create_emission.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
@AndroidEntryPoint
class CreateEmissionActivity : AppCompatActivity() {

    private val viewModel: WalletViewModel by viewModels()

    private val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")

    private lateinit var startDate : Date
    var emissionDirection = EmissionDirection.Add
    var repeatRule = EmissionRepeatRule.Once

    var walletId : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_emission)
        walletId = intent.getIntExtra("walletId", 0)
        bindDirectionAdapter()
        bindStartDatePicker()
        bindRepeatRuleAdapter()

        create_emission_done_button.setOnClickListener {
            onDoneButtonClicked()
        }
    }

    private fun setEmissionStartDate(date: Date){
        startDate = date
        emission_start_date_value.text = dateFormat.format(date)
    }

    private fun showStartDatePickerDialog(){
        val calendar = Calendar.getInstance()
        calendar.time = startDate
        val dialog = DatePickerDialog(
            this, onStartDatePicked,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    private val onStartDatePicked = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(year,month,day)
        setEmissionStartDate(calendar.time)
    }

    private fun bindStartDatePicker(){
        val today = Calendar.getInstance().time
        setEmissionStartDate(today)
        create_emission_select_date_button.setOnClickListener {
            showStartDatePickerDialog()
        }
    }

    private fun bindRepeatRuleAdapter(){
        val repeatRuleAdapter = ArrayAdapter<EmissionRepeatRule>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            EmissionRepeatRule.values()
        )
        emission_repeat_rule_spinner.adapter = repeatRuleAdapter
        emission_repeat_rule_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selected = repeatRuleAdapter.getItem(p2)
                selected?.let { repeatRule = it }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {     }
        }
    }

    private fun bindDirectionAdapter(){
        val directionAdapter = ArrayAdapter<EmissionDirection>(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            EmissionDirection.values()
        )
        emission_direction_spinner.adapter = directionAdapter
        emission_direction_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val selected = directionAdapter.getItem(p2)
                selected?.let { emissionDirection = it }
            }
            override fun onNothingSelected(p0: AdapterView<*>?) {     }
        }
    }

    private fun onDoneButtonClicked(){
        if(checkFieldsCorrect()){
            val emissionData = EmissionData(
                emissionDirection,
                startDate,
                emission_value_et.text.toString().toInt()
            )
            val emission = MoneyEmission(
                emission_title_et.text.toString(),
                walletId,
                emissionData
            )
            emission.repeatRule = repeatRule
            viewModel.insertEmission(emission)
            goToWallet()
        }
        else{
            onFieldsError()
        }
    }

    private fun checkFieldsCorrect() : Boolean{
        return emission_title_et.text.isNotEmpty()
    }

    private fun onFieldsError(){
        Toast.makeText(this, "Something is wrong, check fields!", Toast.LENGTH_SHORT).show()
    }

    private fun goToWallet(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("frgToLoad", 1)
        startActivity(intent)
        overridePendingTransition(0, R.anim.slide_out_anim);
    }
}