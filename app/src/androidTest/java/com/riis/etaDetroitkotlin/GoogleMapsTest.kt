package com.riis.etaDetroitkotlin

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
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
 * Test verifies that google maps appears on the route stops page
 * Created by csollars 3/11/19
 * Updated by josebgs 6/22/21
 *
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class GoogleMapsTest {

    @get:Rule
    var mActivityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)


    @Test
    fun googleMapsTest() {

        // click smartbus
        onView(allOf(withId(R.id.company_name), withText("SmartBus"), isDisplayed()))
            .perform(click())

        // click route 125
        onView(allOf(withId(R.id.route_number), withText("ROUTE 125")))
            .perform(click())

        // check if Google Map is displayed
        onView(withIndex(withContentDescription("Google Map"), 0))
            .check(matches(isDisplayed()))

        pressBack()
        pressBack()

        // click ddot
        onView(allOf(withId(R.id.company_name), withText("DDOT"), isDisplayed()))
            .perform(click())

        // click route 1
        onView(allOf(withId(R.id.route_number), withText("ROUTE 1")))
            .perform(click())

        // check if google maps is displayed
        onView(withIndex(withContentDescription("Google Map"), 0))
            .check(matches(isDisplayed()))

        pressBack()
        pressBack()


        // click fast
        onView(allOf(withId(R.id.company_name), withText("FAST"), isDisplayed()))
            .perform(click())

        // click route 261
        onView(allOf(withId(R.id.route_number), withText("ROUTE 261")))
            .perform(click())

        // check if google maps is displayed
        onView(withIndex(withContentDescription("Google Map"), 0))
            .check(matches(isDisplayed()))

        pressBack()
        pressBack()

        // click people mover
        onView(allOf(withId(R.id.company_name), withText("People Mover"), isDisplayed()))
            .perform(click())

        //check if google maps is displayed
        onView(withIndex(withContentDescription("Google Map"), 0))
            .check(matches(isDisplayed()))

        pressBack()

        // click qline
        onView(allOf(withId(R.id.company_name), withText("QLine"), isDisplayed()))
            .perform(click())

        // check if google maps is displayed
        onView(withIndex(withContentDescription("Google Map"), 0))
            .check(matches(isDisplayed()))
    }

    private fun withIndex(matcher: Matcher<View>, index: Int): Matcher<View> {
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
