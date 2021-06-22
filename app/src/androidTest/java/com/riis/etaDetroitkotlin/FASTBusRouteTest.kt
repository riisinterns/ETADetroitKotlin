package com.riis.etaDetroitkotlin

import android.view.View
import androidx.test.espresso.Espresso.onView
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
 * This test clicks the FAST bus link on the homepage and verifies route names
 * Updated by csollars 3/1/19
 * Updated by josebgs 6/22/21
 */
@LargeTest
@RunWith(AndroidJUnit4::class)
class FASTBusRouteTest {

    @get:Rule
    var mActivityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun busRouteTest() {

        // opens FAST Bus Routes
        onView(
            allOf(
                withId(R.id.company_name),
                withText("FAST"),
                isDisplayed()
            )
        )
            .perform(click())

        // checks name
        onView(
            allOf(
                withId(R.id.route_name),
                withText("FAST MICHIGAN"),
                isDisplayed()
            )
        )
            .check(matches(withText("FAST MICHIGAN")))

        // checks name
        onView(
            allOf(
                withId(R.id.route_name),
                withIndex(withText("FAST WOODWARD"), 0),
                isDisplayed()
            )
        )
            .check(matches(withText("FAST WOODWARD")))

        // checks name
        onView(
            allOf(
                withId(R.id.route_name),
                withIndex(withText("FAST WOODWARD"), 1),
                isDisplayed()
            )
        )
            .check(matches(withText("FAST WOODWARD")))

        // checks name
        onView(
            allOf(
                withId(R.id.route_name),
                withIndex(withText("FAST GRATIOT"), 0),
                isDisplayed()
            )
        )
            .check(matches(withText("FAST GRATIOT")))

        // checks name
        onView(
            allOf(
                withId(R.id.route_name),
                withIndex(withText("FAST GRATIOT"), 1),
                isDisplayed()
            )
        )
            .check(matches(withText("FAST GRATIOT")))
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
