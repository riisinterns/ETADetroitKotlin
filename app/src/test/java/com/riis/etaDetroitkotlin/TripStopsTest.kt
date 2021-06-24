package com.riis.etaDetroitkotlin

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.riis.etaDetroitkotlin.database.BusDao
import com.riis.etaDetroitkotlin.database.BusDatabase
import com.riis.etaDetroitkotlin.model.*
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class TripStopsTest {
    private lateinit var busDao: BusDao
    private lateinit var db: BusDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BusDatabase::class.java
        ).allowMainThreadQueries().build()
        busDao = db.busDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private fun fromString(string: String?): Date {
        val dt = Date()
        if (string != null) {
            dt.hours = string.substring(0, 2).toInt()
            dt.minutes = string.substring(3, 5).toInt()
            dt.seconds = 0
        }
        return dt
    }

    @Test
    @Throws(Exception::class)
    fun writeRouteAndCompanyThenReadList() {
        val company = Company(2, "DDOT", "#054839", "ddot_bus")
        val route =
            Routes(53, 1, company.id, "VERNOR", "Rosa Parks Transit Center to Michgan & Schaefer")
        val stop = Stops(23, "Washington & Michigan", 42.331399, -83.051226)
        val daysOfOperation = DaysOfOperation(1, "weekday")
        val direction = Directions(4, "Eastbound")
        val routeStop = RouteStops(17646, route.id, stop.id, direction.id, daysOfOperation.id)
        val trip = Trips(2277, 158479010, route.id, direction.id)
        val tripStop = TripStops(trip.id, stop.id, fromString("04:19:11"), 2)

        busDao.addCompany(company)
        busDao.addRoute(route)
        busDao.addStop(stop)
        busDao.addDaysOfOperation(daysOfOperation)
        busDao.addDirection(direction)
        busDao.addRouteStop(routeStop)
        busDao.addTrips(trip)
        busDao.addTripStop(tripStop)

        val tripsStopLiveData: LiveData<List<TripStops>> =
            busDao.getTripStops(route.id, direction.id, daysOfOperation.id, stop.id)
        tripsStopLiveData.observeForever { tripStops ->
            assertThat(tripStops[0].tripId, `is`(trip.id))
            assertThat(tripStops[0].stopId, `is`(stop.id))
            assertThat(tripStops[0].arrivalTime.toString(), `is`(fromString("04:19:11").toString()))
            assertThat(tripStops[0].stopSequence, `is`(2))
        }
    }
}
