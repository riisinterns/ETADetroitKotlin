package com.riis.etaDetroitkotlin


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * This test clicks the DDOT link on the homepage and verifies route names
 * Created by itimmis on 12/7/17.
 * Updated by csollars on 3/1/19
 * Updated by josebgs on 6/21/21
 */

@RunWith(AndroidJUnit4::class)
class DDOTBusRouteTest {
    @get:Rule
    var mActivityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun ddotBusRouteTest() {

        onView(allOf(withId(R.id.company_name), withText("DDOT"), isDisplayed()))
            .perform(click())

        onView(allOf(withId(R.id.route_name), withText("VERNOR"), isDisplayed()))
            .check(matches(withText("VERNOR")))

        onView(allOf(withId(R.id.route_name), withText("MICHIGAN"), isDisplayed()))
            .check(matches(withText("MICHIGAN")))

        onView(allOf(withId(R.id.route_name), withText("GRAND RIVER"), isDisplayed()))
            .check(matches(withText("GRAND RIVER")))

        onView(allOf(withId(R.id.route_name), withText("WOODWARD"), isDisplayed()))
            .check(matches(withText("WOODWARD")))

        onView(allOf(withId(R.id.route_name), withText("VAN DYKE/LAFAYETTE"), isDisplayed()))
            .check(matches(withText("VAN DYKE/LAFAYETTE")))
    }
}