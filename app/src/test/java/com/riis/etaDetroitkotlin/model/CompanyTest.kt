package com.riis.etaDetroitkotlin.model

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
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
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.annotation.Config
import java.io.IOException

@RunWith(AndroidJUnit4::class)
@Config(manifest = Config.NONE)
class CompanyTest {
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
    fun writeCompanyAndReadList() {
        val company = Company(1, "SmartBus", "#BC0E29", "smart")
        busDao.addCompany(company)
        val companies: LiveData<List<Company>> = busDao.getCompanies()

        companies.observeForever { c ->
            assertThat(c[0].id, `is`(1))
            assertThat(c[0].name, `is`("SmartBus"))
            assertThat(c[0].brandColor, `is`("#BC0E29"))
            assertThat(c[0].busImgUrl, `is`("smart"))
        }
    }
}
