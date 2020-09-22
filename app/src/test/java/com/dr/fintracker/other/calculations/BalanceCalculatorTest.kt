package com.dr.fintracker.other.calculations

import com.dr.fintracker.data.entities.EmissionData
import com.dr.fintracker.data.entities.EmissionDirection
import com.dr.fintracker.data.entities.EmissionRepeatRule
import com.dr.fintracker.data.entities.MoneyEmission
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.util.*

class BalanceCalculatorTest {

    private lateinit var calculator : BalanceCalculator
    private lateinit var startDate : Date
    private lateinit var endDate : Date

    private val defaultEmissionValue = 300

    @Before
    fun setup(){
        startDate = getDefaultStartDate()
        endDate = getDefaultEndDate()
        calculator = BalanceCalculator(listOf(), startDate, endDate)
    }

    fun getDefaultStartDateCalendar() : Calendar{
        val cal = Calendar.getInstance()
        cal[Calendar.YEAR] = 2020
        cal[Calendar.MONTH] = 1
        cal[Calendar.DAY_OF_MONTH] = 1
        return cal
    }

    fun getDefaultStartDate() : Date{
        return getDefaultStartDateCalendar().time
    }

    fun getDefaultEndDate() : Date{
        val defaultCalendar = getDefaultStartDateCalendar()
        defaultCalendar.add(Calendar.MONTH,1)
        return defaultCalendar.time
    }

    @Test
    fun `empty emission list returns zero result`(){
        val zeroBalance = runBlocking {calculator.calculateBalance()}
        assertEquals(zeroBalance, 0)
    }

    @Test
    fun `unit emission calculates correctly`(){
        val defaultCalendar = getDefaultStartDateCalendar()
        defaultCalendar.add(Calendar.DAY_OF_MONTH, 5)
        val emissionDate = defaultCalendar.time
        val emissionData = EmissionData(EmissionDirection.Add, emissionDate, defaultEmissionValue)
        val unitEmission = MoneyEmission("Test Emission", 0, emissionData)
        calculator.emissionsList = listOf<MoneyEmission>(unitEmission)
        val balance = runBlocking {calculator.calculateBalance()}
        assertEquals(balance, defaultEmissionValue)
    }

//    @Test
//    fun `daily emission calculates correctly`(){
//        val defaultCalendar = getDefaultStartDateCalendar()
//        defaultCalendar.add(Calendar.DAY_OF_MONTH, 5)
//        val emissionDate = defaultCalendar.time
//        val emissionData = EmissionData(EmissionDirection.Add, emissionDate, defaultEmissionValue)
//        val unitEmission = MoneyEmission("Test Emission", 0, emissionData)
//        unitEmission.repeatRule = EmissionRepeatRule.EveryDay
//        calculator.emissionsList = listOf(unitEmission)
//        val balance = runBlocking {calculator.calculateBalance()}
//        assertEquals(balance, defaultEmissionValue*26)
//    }
}