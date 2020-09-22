package com.dr.fintracker.other

import androidx.room.TypeConverter
import java.util.*

class DateConverter {
    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.getTime()
    }
    @TypeConverter
    fun dateToTimestamp(date: Long?): Date? {
        return date?.let { Date(it) }
    }
}