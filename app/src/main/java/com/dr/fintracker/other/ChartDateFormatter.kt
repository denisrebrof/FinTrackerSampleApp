package com.dr.fintracker.other

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.utils.ViewPortHandler
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class ChartDateFormatter : ValueFormatter() {

    private val dateFormat: DateFormat = SimpleDateFormat("dd.MM.yyyy")

    override fun getFormattedValue(
        value: Float,
        entry: Entry?,
        dataSetIndex: Int,
        viewPortHandler: ViewPortHandler?
    ): String {
        return getDateFromFloat(value)
    }

    private fun getDateFromFloat(value : Float) : String{
        val entryDate = Date(value.toLong())
        return dateFormat.format(entryDate)
    }

    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return getDateFromFloat(value)
    }

    override fun getFormattedValue(value: Float): String {
        return getDateFromFloat(value)
    }

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return getDateFromFloat(value)
    }
}