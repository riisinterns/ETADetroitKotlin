package com.riis.etaDetroitkotlin.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.riis.etaDetroitkotlin.model.*


@Database(
    entities = [
        Company::class,
        DaysOfOperation::class,
        Directions::class,
        Routes::class,
        RouteStops::class,
        Stops::class,
        TripDaysOfOperation::class,
        Trips::class,
        TripStops::class
    ],
    version = 1
)
//@TypeConverters(BusTypeConverter::class)
abstract class BusDatabase : RoomDatabase() {
    abstract fun busDao(): BusDao
}