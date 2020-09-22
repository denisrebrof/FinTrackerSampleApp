package com.dr.fintracker.ui.createtarget

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.dr.fintracker.R
import com.dr.fintracker.data.entities.FinTarget
import com.dr.fintracker.ui.MainActivity
import com.dr.fintracker.viewmodel.FinTargetViewModel
import com.dr.fintracker.ui.targetslist.TargetsListFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_create_target.*
@AndroidEntryPoint
class CreateTargetActivity : AppCompatActivity() {

    private val viewModel: FinTargetViewModel by viewModels()

    private var targetPosition = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_target)
        targetPosition = intent.getIntExtra("targetPosition",0)
        create_target_done_button.setOnClickListener {
            onDoneButtonClicked()
        }
    }

    private fun onDoneButtonClicked(){
        if(checkFieldsCorrect()){
            val target = FinTarget(create_target_title_et.text.toString())
            target.cost = create_target_cost_et.text.toString().toInt()
            target.position = targetPosition
            viewModel.insert(target)
            goToTargetsList()
        }
        else{
            onFieldsError()
        }
    }

    private fun goToTargetsList(){
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("frgToLoad", 2)
        startActivity(intent)
        overridePendingTransition(0,R.anim.slide_out_anim);
    }

    private fun checkFieldsCorrect() : Boolean{
        return !create_target_title_et.text.isEmpty() && !create_target_cost_et.text.isEmpty()
    }

    private fun onFieldsError(){
        Toast.makeText(this, "Something is wrong, check fields!", Toast.LENGTH_SHORT).show()
    }
}