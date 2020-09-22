package com.dr.fintracker.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.dr.fintracker.other.DateConverter
import com.dr.fintracker.other.EmissionDirectionConverter
import com.dr.fintracker.other.EmissionRepeatRuleConverter
import java.util.*

@Entity(tableName = "emissions")
@TypeConverters(DateConverter::class, EmissionDirectionConverter::class, EmissionRepeatRuleConverter::class)
data class MoneyEmission(
    var title : String,
    var walletId : Int,
    @Embedded
    var emissionData : EmissionData
){
    var repeatRule : EmissionRepeatRule = EmissionRepeatRule.Once

    @PrimaryKey(autoGenerate = true)
    var emissionId = 0
}

enum class EmissionDirection {
    Add, Spend
}

enum class EmissionRepeatRule{
    Once, EveryDay, EveryWeek, EveryMonth, EveryYear
}