package com.riis.etaDetroitkotlin

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.riis.etaDetroitkotlin.database.BusDao
import com.riis.etaDetroitkotlin.database.BusDatabase
import com.riis.etaDetroitkotlin.model.RouteStopInfo
import com.riis.etaDetroitkotlin.model.Routes
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@Config(manifest=Config.NONE)
class RouteStopInfoTest {
    private lateinit var busDao: BusDao
    private lateinit var db: BusDatabase

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, BusDatabase::class.java).allowMainThreadQueries().build()
        busDao = db.busDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    @Throws(Exception::class)
    fun writeRouteAndReadList() {
//        val route = Routes(53, 1, 2, "VERNOR", "Rosa Parks Transit Center to Michgan & Schaefer")
//        busDao.addRoute(route)
//        val routeStopInfo: LiveData<List<RouteStopInfo>> = busDao.getStopsInfoOnRoute(route.id)
//
//        routeStopInfo.observeForever { r ->
//            assertThat(r[0].routeId, `is`(53))
//        }
    }
}
