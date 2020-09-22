package com.dr.fintracker.other

import androidx.room.TypeConverter
import com.dr.fintracker.data.entities.EmissionRepeatRule

class EmissionRepeatRuleConverter {
    @TypeConverter
    fun toRepeatRule(value: String) = enumValueOf<EmissionRepeatRule>(value)
    @TypeConverter
    fun fromRepeatRule(value: EmissionRepeatRule) = value.name
}