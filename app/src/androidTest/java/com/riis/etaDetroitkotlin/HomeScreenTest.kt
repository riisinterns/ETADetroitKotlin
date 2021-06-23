package com.riis.etaDetroitkotlin

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.hamcrest.Matchers.instanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Verifies the elements on the homepage are displayed
 * Updated by csollars 2/28/19
 * Updated by josebgs 6/22/21
 */

@LargeTest
@RunWith(AndroidJUnit4::class)
class HomeScreenTest {

    @get:Rule
    var mActivityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun homeScreenTest_AppBar() {
        onView(allOf(instanceOf(TextView::class.java), withParent(withId(R.id.app_bar))))
            .check(matches(withText("ETA Detroit Kotlin")))
    }

    @Test
    fun homeScreenTest_SmartBus() {
        onView(allOf(withId(R.id.company_name), withText("SmartBus"), isDisplayed()))
            .check(matches(withText("SmartBus")))
    }

    @Test
    fun homeScreenTest_DDOT() {
        onView(allOf(withId(R.id.company_name), withText("DDOT"), isDisplayed()))
            .check(matches(withText("DDOT")))
    }

    @Test
    fun homeScreenTest_FAST() {
        onView(allOf(withId(R.id.company_name), withText("FAST"), isDisplayed()))
            .check(matches(withText("FAST")))
    }

    @Test
    fun homeScreenTest_People_Mover() {
        onView(allOf(withId(R.id.company_name), withText("People Mover"), isDisplayed()))
            .check(matches(withText("People Mover")))
    }

    @Test
    fun homeScreenTest_QLine() {
        onView(allOf(withId(R.id.company_name), withText("QLine"), isDisplayed()))
            .check(matches(withText("QLine")))
    }

    @Test
    fun homeScreenTest_Route_Map() {
        onView(allOf(withId(R.id.company_name), withText("Route Map"), isDisplayed()))
            .check(matches(withText("Route Map")))
    }
}
