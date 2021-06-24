package com.riis.etaDetroitkotlin.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
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

    @Query("SELECT * FROM trips WHERE id=(:tripId)")
    fun getTrips(tripId: Int): LiveData<Trips>

    @Query("SELECT S.stop_id, TS.arrival_time, stop_sequence, TS.trip_id FROM route_stops RS, stops S, days_of_operation D, routes R, trip_stops TS, directions DIR, trips T WHERE RS.stop_id = S.stop_id  and RS.route_id = (:routeId) and D.id = RS.day_id and RS.route_id = R.id and TS.stop_id = RS.stop_id and DIR.id = RS.direction_id and RS.direction_id = (:directionId) and RS.day_id = (:dayId) and rs.stop_id = (:stopId) and T.route_id = RS.route_id and T.direction_id = RS.direction_id and T.id = TS.trip_id")
    fun getArrivalTimes(
        routeId: Int,
        directionId: Int,
        dayId: Int,
        stopId: Int
    ): LiveData<List<TripStops>>

    @Insert
    fun addCompany(company: Company)
}