package com.dr.fintracker.other

import androidx.room.TypeConverter
import com.dr.fintracker.data.entities.EmissionDirection

class EmissionDirectionConverter {
    @TypeConverter
    fun toEmissionDirection(value: String) = enumValueOf<EmissionDirection>(value)
    @TypeConverter
    fun fromEmissionDirection(value: EmissionDirection) = value.name
}