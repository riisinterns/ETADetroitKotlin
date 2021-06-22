package com.riis.etaDetroitkotlin

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * This test clicks on QLine, verifies the first three stops on the route and Google Map displays
 * Created by csollars 3/5/19
 * Updated by josebgs 6/22/21
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class QLineRouteTest {

    @get:Rule
    var mActivityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun QLineRouteTest() {

        // click on qline
        onView(
            allOf(withId(R.id.company_name), withText("QLine"), isDisplayed())
        )
            .perform(click())

        // check that congress station is displayed
        onView(
            allOf(
                withId(R.id.stop_name),
                withText("Congress Station")
            )
        )
            .check(matches(isDisplayed()))

        // check that campus martius station is displayed
        onView(
            allOf(
                withId(R.id.stop_name),
                withText("Campus Martius Station")
            )
        )
            .check(matches(isDisplayed()))

        // check that grand station is displayed
        onView(
            allOf(
                withId(R.id.stop_name),
                withText("Grand Circus Station")
            )
        )
            .check(matches(isDisplayed()))

        // check that google map is displayed
        onView(allOf(withContentDescription("Google Map"), isDisplayed()))
            .check(matches(isDisplayed()))
    }
}