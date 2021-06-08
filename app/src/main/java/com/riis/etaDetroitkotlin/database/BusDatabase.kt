package com.riis.etaDetroitkotlin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.riis.etaDetroitkotlin.model.Company


@Database(entities =[Company::class], version=1)
abstract class BusDatabase: RoomDatabase() {
    abstract fun busDao(): BusDao
}