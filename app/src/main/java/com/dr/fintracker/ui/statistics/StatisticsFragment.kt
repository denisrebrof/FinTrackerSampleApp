package com.dr.fintracker.ui.statistics

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.dr.fintracker.R
import com.dr.fintracker.data.entities.MoneyEmission
import com.dr.fintracker.data.entities.Wallet
import com.dr.fintracker.other.ChartDateFormatter
import com.dr.fintracker.other.calculations.BalanceCalculator
import com.dr.fintracker.viewmodel.FinTargetViewModel
import com.dr.fintracker.viewmodel.WalletViewModel
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.google.android.material.datepicker.MaterialDatePicker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class StatisticsFragment : Fragment() {

    private val walletViewModel : WalletViewModel by viewModels()
    private val finTargetViewModel: FinTargetViewModel by viewModels()

    private lateinit var balanceCalculator : BalanceCalculator
    private var startBalance = 0

    private var startDate: Date? = null
    private var endDate:Date? = null

    private val chartPeriod = 30

    private val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")
    private var calculateChartJob : Job? = null

    var dataSet : LineDataSet? = null

    private fun bindBalance(){
        val calendar = Calendar.getInstance()
        val today = calendar.time
        calendar.add(Calendar.MONTH,1)
        val nextMonth = calendar.time
        balanceCalculator = BalanceCalculator(listOf(),today,nextMonth)
        setChartRange(today,nextMonth)
    }

    private fun onSelectedWalletUpdates(wallet : Wallet?){
        wallet?.let {
            startBalance = wallet.balance
            recalculateChart()
        }
    }

    private fun onEmissionsUpdates(emissions : List<MoneyEmission>?) {
        emissions?.let {
            balanceCalculator.let { calculator ->
                calculator.emissionsList = it
                recalculateChart()
            }
        }
    }

    private fun setChartRange(start: Date?, end: Date?){
        startDate = start
        endDate = end
        val rangeText = "${dateFormat.format(start)} - ${dateFormat.format(end)}"
        statistics_set_chart_range_btn.text = rangeText
        recalculateChart()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        activity?.title = getString(R.string.stats_menu_item);
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    private fun updateChart(entries: List<Entry> = listOf()){

        chart.xAxis.valueFormatter = ChartDateFormatter()
        chart.xAxis.labelCount = 5
//      chart.xAxis.position = XAxis.XAxisPosition.BOTTOM

        dataSet = LineDataSet(entries, "Label")
        dataSet?.let {
            it.color = R.color.colorPrimary
            it.valueTextColor = android.R.color.black
            it.label = "Balance"
        }

        val lineData = LineData(dataSet)
        chart.description.isEnabled = false
        chart.data = lineData
        chart.invalidate()
    }

    private fun stopCalculations(){
        calculateChartJob?.let {
            if(!it.isCompleted) {
                it.cancel()
            }
        }
    }

    private fun recalculateChart(){
        stopCalculations()
        if(startDate!=null && endDate!=null){
            calculateChartJob = GlobalScope.launch {
                val entries = mutableListOf<Entry>()

                val startTime = startDate!!.time
                val endTime = endDate!!.time

                val period = (endTime-startTime)/ chartPeriod
                var nextCalcTime = startTime

                while (nextCalcTime<endTime){
                    val nextCalcDate = Date(nextCalcTime)
                    entries.add(getBalanceEntry(nextCalcDate))
                    nextCalcTime+=period
                }
                updateChart(entries)
            }
        }
    }

    private suspend fun getBalanceEntry(date : Date) : Entry{
        balanceCalculator.endDate = date;
        val calculatedBalance = balanceCalculator.calculateBalance()
        return Entry(date.time.toFloat(), calculatedBalance.toFloat())
    }

    override fun onDestroy() {
        stopCalculations()
        super.onDestroy()
    }

    override fun onStop() {
        stopCalculations()
        super.onStop()
    }

    private fun updateChart(){

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        walletViewModel.getSelectedWallet().observe(viewLifecycleOwner, Observer { onSelectedWalletUpdates(it) })
        walletViewModel.getSelectedWalletEmissions().observe(viewLifecycleOwner, Observer { onEmissionsUpdates(it) })
        bindBalance()
        statistics_set_chart_range_btn.setOnClickListener {showDateRangePicker()}
        updateChart()
    }

    private fun showDateRangePicker(){
        val builder = MaterialDatePicker.Builder.dateRangePicker()
        builder.setSelection(androidx.core.util.Pair(startDate?.time, endDate?.time))
        val picker = builder.build()
        picker.show(activity?.supportFragmentManager!!, picker.toString())
        picker.addOnPositiveButtonClickListener {
            val startPickedDate = it.first?.let { it1 -> Date(it1) }
            val endPickedDate = it.second?.let { it -> Date(it) }
            setChartRange(startPickedDate,endPickedDate)
        }
    }
}