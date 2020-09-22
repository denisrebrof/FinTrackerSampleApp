package com.dr.fintracker.ui.targetslist

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.dr.fintracker.R
import com.dr.fintracker.data.entities.FinTarget
import com.dr.fintracker.data.entities.MoneyEmission
import com.dr.fintracker.data.entities.Wallet
import com.dr.fintracker.ui.createtarget.CreateTargetActivity
import com.dr.fintracker.other.calculations.BalanceCalculator
import com.dr.fintracker.viewmodel.FinTargetViewModel
import com.dr.fintracker.viewmodel.WalletViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_targets_list.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@AndroidEntryPoint
class TargetsListFragment : Fragment() {

    private val walletViewModel : WalletViewModel by viewModels()
    private val finTargetViewModel: FinTargetViewModel by viewModels()
    private var adapter : TargetsListAdapter? = null
    private var targetPosition = 0

    private var balanceCalculator : BalanceCalculator? = null
    private var calculateBalanceJob : Job? = null

    var walletBalance = 0
    var additionalBalance = 0

    private val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")

    private var endDate : Date? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.targets_menu_item);
        return inflater.inflate(R.layout.fragment_targets_list, container, false)
    }

    private fun bindBalance(){
        val calendar = Calendar.getInstance()
        val today = calendar.time
        calendar.add(Calendar.MONTH,1)
        val nextMonth = calendar.time
        balanceCalculator = BalanceCalculator(listOf(),today,nextMonth)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bindBalance()
        bindEndDatePicker()

        adapter = TargetsListAdapter(listOf(), finTargetViewModel)
        targets_list_recycler_view.adapter = adapter
        targets_list_recycler_view.layoutManager = LinearLayoutManager(context)
        finTargetViewModel.getAllTargets().observe(viewLifecycleOwner, Observer { onTargetsUpdates(it) })

        val touchHelperCallback = TargetListTouchHelperCallback(adapter,activity?.supportFragmentManager)
        val touchHelper = ItemTouchHelper(touchHelperCallback)
        touchHelper.attachToRecyclerView(targets_list_recycler_view)

        walletViewModel.getSelectedWallet().observe(viewLifecycleOwner, Observer { onSelectedWalletUpdates(it) })
        walletViewModel.getSelectedWalletEmissions().observe(viewLifecycleOwner, Observer { onEmissionsUpdates(it) })

        targets_list_fab.setOnClickListener {
            onCreateNewTask()
        }
    }

    private fun bindEndDatePicker(){
        val today = Calendar.getInstance().time
        updateEndDate(today)
        set_end_date_and_calculate_btn.setOnClickListener {
            showStartDatePickerDialog()
        }
    }

    private fun updateEndDate(date : Date){
        endDate = date
        set_end_date_and_calculate_btn.text = dateFormat.format(date)
        balanceCalculator?.let {
            it.endDate = date
            calculateBalance()
        }
    }

    private val onStartDatePicked = DatePickerDialog.OnDateSetListener { _, year, month, day ->
        val calendar = Calendar.getInstance()
        calendar.set(year,month,day)
        updateEndDate(calendar.time)
    }

    private fun showStartDatePickerDialog(){
        val calendar = Calendar.getInstance()
        calendar.time = endDate
        val dialog = DatePickerDialog(
            requireContext(), onStartDatePicked,
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH)
        )
        dialog.show()
    }

    private fun onSelectedWalletUpdates(selectedWallet: Wallet?) {
        selectedWallet?.balance?.let { walletBalance = it }
        updateBalanceForecast()
    }

    private fun updateBalanceForecast(){
        val forecastedBalance = walletBalance+additionalBalance
        wallet_balance_value.text = walletBalance.toString()
        wallet_additional_balance_value.text = (forecastedBalance).toString()
        val textColor = when(additionalBalance){
            0 -> android.R.color.darker_gray
            in Int.MIN_VALUE until 0 -> R.color.negative
            else -> R.color.positive
        }
        wallet_additional_balance_value.setTextColor(ContextCompat.getColor(requireContext(), textColor))

        adapter?.let {
            it.forecastedBalance = forecastedBalance
            it.notifyDataSetChanged()
        }
    }

    private fun onTargetsUpdates(it: List<FinTarget>) {
        targets_list_no_targets_hint.isVisible = it.count() == 0
        targetPosition = it.count()
        adapter?.targets = it
        adapter?.notifyDataSetChanged()
    }

    private fun calculateBalance(){
        balanceCalculator?.let { calculator ->
            calculateBalanceJob = GlobalScope.launch(Dispatchers.Main) {
                additionalBalance = calculator.calculateBalance()
                updateBalanceForecast()
            }
        }
    }

    private fun onEmissionsUpdates(emissions: List<MoneyEmission>?) {
        emissions?.let {
            balanceCalculator?.let { calculator ->
                wallet_additional_balance_value.text = "* * *"
                context?.let {wallet_additional_balance_value.setTextColor(ContextCompat.getColor(it, R.color.colorAccent))}
                calculator.emissionsList = it
                calculateBalance()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        adapter?.updateListPositions()
        calculateBalanceJob?.cancel()
    }

    override fun onDestroy() {
        super.onDestroy()
        adapter?.updateListPositions()
        calculateBalanceJob?.cancel()
    }

    private fun onCreateNewTask(){
        val intent = Intent(context, CreateTargetActivity::class.java)
        intent.putExtra("targetPosition", targetPosition)
        startActivity(intent)
//        activity?.overridePendingTransition(R.anim.slide_in_anim, 0);
    }
}