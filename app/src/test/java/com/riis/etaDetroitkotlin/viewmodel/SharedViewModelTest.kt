package com.riis.etaDetroitkotlin.viewmodel

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.riis.etaDetroitkotlin.SharedViewModel
import com.riis.etaDetroitkotlin.database.BusDao
import com.riis.etaDetroitkotlin.database.BusDatabase
import com.riis.etaDetroitkotlin.database.BusRepository
import com.riis.etaDetroitkotlin.model.*
import junit.framework.TestCase
import org.hamcrest.CoreMatchers
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.IOException
import java.lang.Exception
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class SharedViewModelTest : TestCase() {

    private var busDao: BusDao? = null
    private var db: BusDatabase? = null
    private var viewModel: SharedViewModel? = null

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BusDatabase::class.java
        ).allowMainThreadQueries().build()
        busDao = db?.busDao()

        BusRepository.initialize(context)
        BusRepository.get().setDatabase(db!!)
        BusRepository.get().setBusDao(busDao!!)

        viewModel = SharedViewModel()
        print("HERE")
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db?.close()
        db = null
        busDao = null
        viewModel = null
    }

    @Test
    fun testGetCompanyListLiveData() {
        Thread.sleep(1000)
        assertEquals(4, 2+2)
        Thread.sleep(1000)
    }

    fun testGetRouteListLiveData() {}

    fun testSetRouteListLiveData() {}

    fun testGetRouteStopsInfoListLiveData() {}

    fun testSetRouteStopsInfoListLiveData() {}

    @Test
    fun testGetArrivalTimes() {

        Thread.sleep(1000)
        val company = Company(2, "DDOT", "#054839", "ddot_bus")
        val route =
            Routes(53, 1, company.id, "VERNOR", "Rosa Parks Transit Center to Michgan & Schaefer")
        val stop = Stops(23, "Washington & Michigan", 42.331399, -83.051226)
        val daysOfOperation = DaysOfOperation(1, "weekday")
        val direction = Directions(4, "Eastbound")
        val routeStop = RouteStops(17646, route.id, stop.id, direction.id, daysOfOperation.id)
        val trip = Trips(2277, 158479010, route.id, direction.id)
        val tripStop = TripStops(trip.id, stop.id, fromString("04:19:11"), 2)

        busDao?.addCompany(company)
        busDao?.addRoute(route)
        busDao?.addStop(stop)
        busDao?.addDaysOfOperation(daysOfOperation)
        busDao?.addDirection(direction)
        busDao?.addRouteStop(routeStop)
        busDao?.addTrips(trip)
        busDao?.addTripStop(tripStop)

        viewModel?.getArrivalTimes(route.id, direction.id, daysOfOperation.id, stop.id)?.observeForever { tripStops ->
            assertThat(tripStops[0].tripId, `is`(trip.id))
            assertThat(tripStops[0].stopId, `is`(stop.id))
            assertThat(tripStops[0].arrivalTime.toString(), `is`(fromString("04:19:11").toString()))
            assertThat(tripStops[0].stopSequence, `is`(2))
        }
        Thread.sleep(1000)
    }

    fun testGetCurrentCompany() {}

    fun testGetCurrentRoute() {}

    @Test
    fun testSaveCompany() {
        Thread.sleep(1000)
        val company = Company(2, "DDOT", "#054839", "ddot_bus")
        viewModel?.saveCompany(company)

        assertThat(viewModel?.currentCompany?.id, `is`(2))
        assertThat(viewModel?.currentCompany?.name, `is`("DDOT"))
        assertThat(viewModel?.currentCompany?.brandColor, `is`("#054839"))
        assertThat(viewModel?.currentCompany?.busImgUrl, `is`("ddot_bus"))
        Thread.sleep(1000)
    }

    @Test
    fun testSaveRoute() {
        Thread.sleep(1000)
        val route = Routes(53, 1, 2, "VERNOR", "Rosa Parks Transit Center to Michgan & Schaefer")
        viewModel?.saveRoute(route)

        assertThat(viewModel?.currentRoute?.id, `is`(53))
        assertThat(viewModel?.currentRoute?.number, `is`(1))
        assertThat(viewModel?.currentRoute?.companyId, `is`(2))
        assertThat(viewModel?.currentRoute?.name, `is`("VERNOR"))
        assertThat(viewModel?.currentRoute?.description, `is`("Rosa Parks Transit Center to Michgan & Schaefer"))
        Thread.sleep(1000)
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
}