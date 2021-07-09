package com.riis.etaDetroitkotlin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.riis.etaDetroitkotlin.model.*
import org.jetbrains.annotations.TestOnly

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

    @Query("SELECT * FROM trip_days_of_operation")
    fun getTripDaysOfOperation(): LiveData<List<TripDaysOfOperation>>

    @Query("SELECT * FROM trips where route_id=(:routeId) and direction_id=(:directionId)")
    fun getTrips(routeId: Int, directionId: Int): LiveData<List<Trips>>

    @TestOnly
    @Insert
    fun addRoute(route: Routes)

    @TestOnly
    @Insert
    fun addTrips(trips: Trips)

    @TestOnly
    @Insert
    fun addTripStop(tripStop: TripStops)

    @TestOnly
    @Insert
    fun addCompany(company: Company)

    @TestOnly
    @Insert
    fun addStop(stop: Stops)

    @TestOnly
    @Insert
    fun addDirection(direction: Directions)

    @TestOnly
    @Insert
    fun addDaysOfOperation(daysOfOperation: DaysOfOperation)

    @TestOnly
    @Insert
    fun addRouteStop(routeStop: RouteStops)

    @TestOnly
    @Insert
    fun addTripDaysOfOperation(tripDaysOfOperation: TripDaysOfOperation)
}