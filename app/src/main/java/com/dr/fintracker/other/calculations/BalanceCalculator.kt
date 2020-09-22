package com.dr.fintracker.other.calculations

import com.dr.fintracker.data.entities.EmissionData
import com.dr.fintracker.data.entities.EmissionDirection
import com.dr.fintracker.data.entities.EmissionRepeatRule
import com.dr.fintracker.data.entities.MoneyEmission
import java.util.*

class BalanceCalculator(
    var emissionsList: List<MoneyEmission>,
    var startDate: Date,
    var endDate: Date
) {

    private val calendar : Calendar = Calendar.getInstance()

    suspend fun calculateBalance() : Int{
        if(startDate.after(endDate))
            return 0
        var balance = 0
        for(emission in emissionsList){
            balance+=calculateEmission(emission)*if(emission.emissionData.direction==EmissionDirection.Add) 1 else -1
        }
        return balance
    }

    suspend fun calculateEmission(emission: MoneyEmission) : Int{
        val emissionData = emission.emissionData
        if(emissionData.startDate.after(endDate))
        return 0

        return when(emission.repeatRule){
            EmissionRepeatRule.Once -> calculateUnitaryEmission(emissionData)
            EmissionRepeatRule.EveryDay ->  calculateDailyEmission(emissionData)
            EmissionRepeatRule.EveryWeek -> calculateWeeklyEmission(emissionData)
            EmissionRepeatRule.EveryMonth -> calculateMonthlyEmission(emissionData)
            EmissionRepeatRule.EveryYear -> calculateYearlyEmission(emissionData)
        }
    }

    private fun calculateYearlyEmission(emissionData: EmissionData): Int {
        calendar.time = emissionData.startDate
        val emissionDateOfYear = calendar.get(Calendar.DAY_OF_YEAR).coerceIn(0, 365)
        calendar.set(Calendar.DAY_OF_YEAR, emissionDateOfYear)
        var emissionCaseCount = 0
        while (calendar.time.before(endDate)) {
            if (calendar.time.after(startDate))
                emissionCaseCount++
            calendar.add(Calendar.YEAR, 1)
        }
        return emissionCaseCount * emissionData.value
    }

    private fun calculateMonthlyEmission(emissionData: EmissionData): Int {
        calendar.time = emissionData.startDate
        val emissionDateOfMonth = calendar.get(Calendar.DAY_OF_MONTH).coerceIn(0, 30)
        calendar.set(Calendar.DAY_OF_MONTH, emissionDateOfMonth)
        var emissionCaseCount = 0
        while (calendar.time.before(endDate)) {
            if (calendar.time.after(startDate))
                emissionCaseCount++
            calendar.add(Calendar.MONTH, 1)
        }
        return emissionCaseCount * emissionData.value
    }

    private fun calculateWeeklyEmission(emissionData: EmissionData): Int {
        if (emissionData.startDate.before(startDate)) {
            calendar.time = emissionData.startDate;
            val emissionDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            calendar.time = startDate
            val startDayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            calendar.add(Calendar.DATE, (7 + emissionDayOfWeek - startDayOfWeek) % 7)
        }
        val emissionCaseCount =
            (endDate.time - emissionData.startDate.time) / (24 * 60 * 60 * 1000 * 7)
        if (emissionCaseCount < 0)
            return 0
        return emissionCaseCount.toInt() * emissionData.value
    }

    private fun calculateDailyEmission(emissionData: EmissionData): Int {
        val clampedDay = if (emissionData.startDate.before(startDate)) startDate else emissionData.startDate
        val emissionCaseCount = (endDate.time - clampedDay.time) / (24 * 60 * 60 * 1000)
        return emissionCaseCount.toInt() * emissionData.value
    }

    private fun calculateUnitaryEmission(emissionData: EmissionData): Int {
        return if (emissionData.startDate.after(startDate)) emissionData.value else 0
    }
}