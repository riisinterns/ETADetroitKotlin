package com.riis.etaDetroitkotlin.database

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.room.TypeConverter
import java.util.*

class BusTypeConverter {
    @RequiresApi(Build.VERSION_CODES.O)
    @TypeConverter
    fun fromString(string: String?): Date {
        val dt = Date()
        if (string != null) {
            dt.hours = string.substring(0, 2).toInt()
            dt.minutes = string.substring(3, 5).toInt()
            dt.seconds = 0
        }
        return dt
    }

    @TypeConverter
    fun fromDate(date: Date?): String {
        return "${"%02d".format(date?.hours)}:${"%02d".format(date?.minutes)}:${"%02d".format(date?.seconds)}"
    }
}