package com.riis.etaDetroitkotlin.model

import android.content.Context
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
class TripDaysOfOperationTest {
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
    fun writeDataThenTestReadingTripDaysOfOperation() {

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

        val tripDaysOfOperationLiveData = busDao.getTripDaysOfOperation()

        tripDaysOfOperationLiveData.observeForever { tripDaysOfOperationList ->
            assertThat(tripDaysOfOperationList[0].operationDayId, `is`(4))
            assertThat(tripDaysOfOperationList[0].tripId, `is`(trip.id))
        }
    }
}
