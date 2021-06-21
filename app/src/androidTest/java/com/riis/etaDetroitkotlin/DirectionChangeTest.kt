package com.riis.etaDetroitkotlin

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * This test goes into the first route for Smart Bus, DDOT, and FAST. It clicks the directional
 * arrow and verifies that the first stop changes
 *
 * Created by csollars 3/1/19
 * Updated by josebgs 6/21/21
 */

@RunWith(AndroidJUnit4::class)
@LargeTest

class DirectionChangeTest {

    @get:Rule
    var mActivityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun directionChangeTest() {


        onView(allOf(withId(R.id.company_name), withText("SmartBus"), isDisplayed()))
            .perform(click())

        onView(allOf(withId(R.id.route_number), withText("ROUTE 125")))
            .perform(click())

        // locate the first stop
        var stop = onView(allOf(withIndex(withId(R.id.stop_name), 0)))
            .check(matches(isDisplayed()))

        // click the arrow icon
        onView(allOf(withId(R.id.fab), isDisplayed()))
            .perform(click())

        stop.check(doesNotExist())
        // verify the first stop is no longer visible on the screen

        pressBack()
        pressBack()

        onView(allOf(withId(R.id.company_name), withText("DDOT"), isDisplayed()))
            .perform(click())

        onView(allOf(withId(R.id.route_number), withText("ROUTE 1")))
            .perform(click())

        // locate the first stop
        stop = onView(allOf(withIndex(withId(R.id.stop_name), 0)))
            .check(matches(isDisplayed()))

        // click the arrow icon
        onView(allOf(withId(R.id.fab), isDisplayed()))
            .perform(click())
        stop.check(doesNotExist())
        // verify the first stop is no longer visible on screen


        pressBack()
        pressBack()

        onView(allOf(withId(R.id.company_name), withText("FAST"), isDisplayed()))
            .perform(click())

        onView(allOf(withId(R.id.route_number), withText("ROUTE 461")))
            .perform(click())

        stop = onView(allOf(withIndex(withId(R.id.stop_name), 0)))
            .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.fab), isDisplayed()))
            .perform(click())

        stop.check(doesNotExist())
    }

    fun withIndex(matcher: Matcher<View>, index: Int): Matcher<View> {
        return object : TypeSafeMatcher<View>() {
            var currentIndex = 0
            override fun describeTo(description: Description) {
                description.appendText("with index: ")
                description.appendValue(index)
                matcher.describeTo(description)
            }

            override fun matchesSafely(view: View): Boolean {
                return matcher.matches(view) && currentIndex++ == index
            }
        }
    }
}
