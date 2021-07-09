package com.riis.etaDetroitkotlin.viewmodel

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.riis.etaDetroitkotlin.SharedViewModel
import com.riis.etaDetroitkotlin.database.BusDao
import com.riis.etaDetroitkotlin.database.BusDatabase
import com.riis.etaDetroitkotlin.database.BusRepository
import com.riis.etaDetroitkotlin.model.*
import junit.framework.TestCase
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class SharedViewModelTest : TestCase() {

    private lateinit var busDao: BusDao
    private lateinit var db: BusDatabase
    private lateinit var viewModel: SharedViewModel

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BusDatabase::class.java
        ).allowMainThreadQueries().build()
        busDao = db.busDao()

        BusRepository.initialize(context)
        BusRepository.get().setDatabase(db)
        BusRepository.get().setBusDao(busDao)

        viewModel = SharedViewModel()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun testGetCompanyListLiveData() {
        val company = Company(1, "SmartBus", "#BC0E29", "smart")
        busDao.addCompany(company)

        viewModel.companyListLiveData.observeForever { c ->
            assertThat(c[0].id, `is`(1))
            assertThat(c[0].name, `is`("SmartBus"))
            assertThat(c[0].brandColor, `is`("#BC0E29"))
            assertThat(c[0].busImgUrl, `is`("smart"))
        }
        Thread.sleep(100)
    }

    @Test
    fun testGetRouteListLiveData() {
        val company = Company(2, "DDOT", "#054839", "ddot_bus")
        val route =
            Routes(53, 1, company.id, "VERNOR", "Rosa Parks Transit Center to Michgan & Schaefer")
        busDao.addCompany(company)
        busDao.addRoute(route)

        viewModel.routeListLiveData.observeForever { r ->
            assertThat(r[0].id, `is`(53))
            assertThat(r[0].number, `is`(1))
            assertThat(r[0].companyId, `is`(2))
            assertThat(r[0].name, `is`("VERNOR"))
            assertThat(r[0].description, `is`("Rosa Parks Transit Center to Michgan & Schaefer"))
        }
        Thread.sleep(100)
    }

    @Test
    fun testGetRouteStopsInfoListLiveData() {
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

        viewModel.routeStopsInfoListLiveData.observeForever { routesStopInfo ->
            assertThat(routesStopInfo[0].routeId, `is`(route.id))
            assertThat(routesStopInfo[0].stopId, `is`(stop.id))
            assertThat(routesStopInfo[0].directionId, `is`(direction.id))
            assertThat(routesStopInfo[0].dayId, `is`(daysOfOperation.id))
            assertThat(routesStopInfo[0].day, `is`(daysOfOperation.day))
            assertThat(routesStopInfo[0].name, `is`(stop.name))
            assertThat(routesStopInfo[0].latitude, `is`(stop.latitude))
            assertThat(routesStopInfo[0].longitude, `is`(stop.longitude))
        }
        Thread.sleep(100)
    }

    @Test
    fun testGetTrips() {
        val company = Company(2, "DDOT", "#054839", "ddot_bus")
        val route =
            Routes(53, 1, company.id, "VERNOR", "Rosa Parks Transit Center to Michgan & Schaefer")
        val direction = Directions(3, "Westbound")
        val trip = Trips(2328, 158530010, route.id, direction.id)

        busDao.addCompany(company)
        busDao.addRoute(route)
        busDao.addDirection(direction)
        busDao.addTrips(trip)

        viewModel.getTrips(route.id, direction.id).observeForever { trips ->
            assertThat(trips[0].id, `is`(2328))
            assertThat(trips[0].tripId, `is`(158530010))
            assertThat(trips[0].routeId, `is`(route.id))
            assertThat(trips[0].directionId, `is`(direction.id))
        }

        Thread.sleep(100)
    }

    @Test
    fun testGetTripDaysOfOperation() {
        val company = Company(2, "DDOT", "#054839", "ddot_bus")
        val route =
            Routes(53, 1, company.id, "VERNOR", "Rosa Parks Transit Center to Michgan & Schaefer")
        val direction = Directions(3, "Westbound")
        val trip = Trips(2328, 158530010, route.id, direction.id)
        val tripDaysOfOperation = TripDaysOfOperation(4, trip.id)
        val daysOfOperation = DaysOfOperation(4, "sunday")

        busDao.addCompany(company)
        busDao.addRoute(route)
        busDao.addDirection(direction)
        busDao.addTrips(trip)
        busDao.addDaysOfOperation(daysOfOperation)
        busDao.addTripDaysOfOperation(tripDaysOfOperation)

        viewModel.getTripDaysOfOperation().observeForever { tripDaysOfOperationList ->
            assertThat(tripDaysOfOperationList[0].operationDayId, `is`(4))
            assertThat(tripDaysOfOperationList[0].tripId, `is`(trip.id))
        }

        Thread.sleep(100)
    }

    @Test
    fun testGetTripStops() {

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

        viewModel.getTripStops(stop.id)
            .observeForever { tripStops ->
                assertThat(tripStops[0].tripId, `is`(trip.id))
                assertThat(tripStops[0].stopId, `is`(stop.id))
                assertThat(
                    tripStops[0].arrivalTime.toString(),
                    `is`(fromString("04:19:11").toString())
                )
                assertThat(tripStops[0].stopSequence, `is`(2))
            }
        Thread.sleep(100)
    }

    @Test
    fun testSaveCompany() {
        val company = Company(2, "DDOT", "#054839", "ddot_bus")
        viewModel.saveCompany(company)

        assertThat(viewModel.currentCompany?.id, `is`(2))
        assertThat(viewModel.currentCompany?.name, `is`("DDOT"))
        assertThat(viewModel.currentCompany?.brandColor, `is`("#054839"))
        assertThat(viewModel.currentCompany?.busImgUrl, `is`("ddot_bus"))
        Thread.sleep(100)
    }

    @Test
    fun testSaveRoute() {
        val route = Routes(53, 1, 2, "VERNOR", "Rosa Parks Transit Center to Michgan & Schaefer")
        viewModel.saveRoute(route)

        assertThat(viewModel.currentRoute?.id, `is`(53))
        assertThat(viewModel.currentRoute?.number, `is`(1))
        assertThat(viewModel.currentRoute?.companyId, `is`(2))
        assertThat(viewModel.currentRoute?.name, `is`("VERNOR"))
        assertThat(
            viewModel.currentRoute?.description,
            `is`("Rosa Parks Transit Center to Michgan & Schaefer")
        )
        Thread.sleep(100)
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