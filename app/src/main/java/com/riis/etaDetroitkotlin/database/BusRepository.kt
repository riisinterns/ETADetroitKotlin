package com.riis.etaDetroitkotlin.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import com.riis.etaDetroitkotlin.model.*

private const val DATABASE_NAME = "eta_detroit.sqlite"
private const val DATABASE_PATH = "databases/eta_detroit.sqlite"

class BusRepository private constructor(context: Context) {
    private val database: BusDatabase = Room.databaseBuilder(
        context.applicationContext,
        BusDatabase::class.java,
        DATABASE_NAME
    ).createFromAsset(DATABASE_PATH).build()

    private val busDao = database.busDao()

    fun getCompanies(): LiveData<List<Company>> = busDao.getCompanies()
    fun getRoutes(companyId: Int): LiveData<List<Routes>> = busDao.getRoutes(companyId)
    fun getRouteStops(routeId: Int): LiveData<List<RouteStops>> = busDao.getRouteStops(routeId)
    fun getTripStops(stopId: Int): LiveData<List<TripStops>> = busDao.getTripStops(stopId)
    fun getStop(stopId: Int): LiveData<Stops> = busDao.getStop(stopId)
    fun getTrips(tripId: Int): LiveData<Trips> = busDao.getTrips(tripId)
    fun getDays(): LiveData<List<DaysOfOperation>> = busDao.getDays()
//    fun getDirections(directionId: Int): LiveData<Int> = busDao.getDirections(directionId)

    companion object {
        private var INSTANCE: BusRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = BusRepository(context)
            }
        }

        fun get(): BusRepository {
            return INSTANCE ?: throw IllegalStateException("NOT INITIALIZED")
        }
    }
}