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
class TripsTest {
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
    fun writeDataThenTestReadingTripsData() {

        val company = Company(2, "DDOT", "#054839", "ddot_bus")
        val route =
            Routes(53, 1, company.id, "VERNOR", "Rosa Parks Transit Center to Michgan & Schaefer")
        val direction = Directions(3, "Westbound")
        val trip = Trips(2328, 158530010, route.id, direction.id)

        busDao.addCompany(company)
        busDao.addRoute(route)
        busDao.addDirection(direction)
        busDao.addTrips(trip)

        val tripsLiveData = busDao.getTrips(route.id, direction.id)
        tripsLiveData.observeForever { trips ->
            assertThat(trips[0].id, `is`(2328))
            assertThat(trips[0].tripId, `is`(158530010))
            assertThat(trips[0].routeId, `is`(route.id))
            assertThat(trips[0].directionId, `is`(direction.id))
        }
    }
}
