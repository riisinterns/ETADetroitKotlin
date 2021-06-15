package com.riis.etaDetroitkotlin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.riis.etaDetroitkotlin.model.*

@Dao
interface BusDao {

    @Query("SELECT * FROM companies")
    fun getCompanies(): LiveData<List<Company>>

    @Query("SELECT * FROM routes WHERE company_id=(:companyId)")
    fun getRoutes(companyId: Int): LiveData<List<Routes>>

    @Query("SELECT * FROM route_stops WHERE route_id=(:routeId)")
    fun getRouteStops(routeId: Int): LiveData<List<RouteStops>>

    @Query("SELECT * FROM trip_stops WHERE stop_id=(:stopId)")
    fun getTripStops(stopId: Int): LiveData<List<TripStops>>

    @Query("SELECT * FROM stops WHERE stop_id=(:stopId)")
    fun getStop(stopId: Int): LiveData<Stops>

    @Query("SELECT * FROM trips WHERE id=(:tripId)")
    fun getTrips(tripId: Int): LiveData<Trips>

    @Query("SELECT * FROM days_of_operation")
    fun getDays(): LiveData<List<DaysOfOperation>>


}