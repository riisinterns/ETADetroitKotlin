package com.riis.etaDetroitkotlin.database

import androidx.room.TypeConverter
import java.sql.Time

class BusTypeConverter {
    @TypeConverter
    fun fromTime(time: Time?): Long? {
        return time?.time
    }

    @TypeConverter
    fun toTime(time: Long?): Time? {

        return time?.let {
            Time(it)
        }
    }
}