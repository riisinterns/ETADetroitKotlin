package com.riis.etaDetroitkotlin

import android.widget.TextView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
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
 * This test clicks on People Mover, verifies the first three stops on the route and Google Map displays
 * Created by csollars 3/5/19
 * Updated by josebgs 22/6/2021
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class PeopleMoverRouteTest {

    @get:Rule
    var mActivityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun peopleMoverRouteTest() {

        onView(allOf(withId(R.id.company_name), withText("People Mover"), isDisplayed()))
        .perform(click())

        onView(allOf(withId(R.id.stop_name), withText("Times Square")))
        .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.stop_name), withText("Grand Circus")))
        .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.stop_name), withText("Broadway")))
        .check(matches(isDisplayed()))

        onView(allOf(withContentDescription("Google Map"), isDisplayed()))
        .check(matches(isDisplayed()))
    }

}
