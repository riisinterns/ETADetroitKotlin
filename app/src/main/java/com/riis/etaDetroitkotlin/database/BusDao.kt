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

    @Query("SELECT route_id, S.stop_id, direction_id, day_id, day, name, latitude, longitude  FROM route_stops RS, stops S, days_of_operation D WHERE route_id=(:routeId) and S.stop_id = RS.stop_id and D.id = RS.day_id")
    fun getStopsInfoOnRoute(routeId: Int): LiveData<List<RouteStopInfo>>

    @Query("SELECT * FROM trip_stops WHERE stop_id=(:stopId)")
    fun getTripStops(stopId: Int): LiveData<List<TripStops>>

    @Query("SELECT * FROM trips WHERE id=(:tripId)")
    fun getTrips(tripId: Int): LiveData<Trips>



}