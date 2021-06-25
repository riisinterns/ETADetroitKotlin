package com.riis.etaDetroitkotlin.model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.riis.etaDetroitkotlin.database.BusDao
import com.riis.etaDetroitkotlin.database.BusDatabase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class RouteStopInfoTest {
    private lateinit var busDao: BusDao
    private lateinit var db: BusDatabase

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

    @Test
    @Throws(Exception::class)
    fun writeDataThenReadRouteStopInfoList() {
        val company = Company(2, "DDOT", "#054839", "ddot_bus")
        val route =
            Routes(53, 1, company.id, "VERNOR", "Rosa Parks Transit Center to Michgan & Schaefer")
        val stop = Stops(23, "Washington & Michigan", 42.331399, -83.051226)
        val daysOfOperation = DaysOfOperation(1, "weekday")
        val direction = Directions(3, "Westbound")
        val routeStop = RouteStops(17646, route.id, stop.id, direction.id, daysOfOperation.id)

        busDao.addCompany(company)
        busDao.addRoute(route)
        busDao.addStop(stop)
        busDao.addDaysOfOperation(daysOfOperation)
        busDao.addDirection(direction)
        busDao.addRouteStop(routeStop)

        val routeStopInfo: LiveData<List<RouteStopInfo>> = busDao.getStopsInfoOnRoute(route.id)
        routeStopInfo.observeForever { routesStopInfo ->
            assertThat(routesStopInfo[0].routeId, `is`(route.id))
            assertThat(routesStopInfo[0].stopId, `is`(stop.id))
            assertThat(routesStopInfo[0].directionId, `is`(direction.id))
            assertThat(routesStopInfo[0].dayId, `is`(daysOfOperation.id))
            assertThat(routesStopInfo[0].day, `is`(daysOfOperation.day))
            assertThat(routesStopInfo[0].name, `is`(stop.name))
            assertThat(routesStopInfo[0].latitude, `is`(stop.latitude))
            assertThat(routesStopInfo[0].longitude, `is`(stop.longitude))
        }
    }
}
