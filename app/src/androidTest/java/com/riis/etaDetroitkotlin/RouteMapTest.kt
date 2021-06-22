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
 * This tests the route map functionality. It verifies all elements are visible on the page.
 * It verifies that the checkboxes are unchecked by defaults, verifies that they become checked
 * when clicked, and unchecked when clicked again.
 *
 * Created by csollars 3/5/19
 * Updated by josebgs 6/22/21
 *
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class RouteMapTest {

    @get:Rule
    var mActivityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun routeMapTest() {
        onView(allOf(withId(R.id.company_name), withText("Route Map"), isDisplayed()))
            .perform(click())

        onView(allOf(withContentDescription("Google Map")))
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.DdotCheckBox),
                withText("Ddot Bus Route")
            )
        )
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.SmartCheckBox),
                withText("Smart Bus Route")
            )
        )
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.ReflexCheckBox),
                withText("Fast Bus Route")
            )
        )
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.PeopleMoverCheckBox),
                withText("Detroit People Mover")
            )
        )
            .check(matches(isDisplayed()))

        onView(
            allOf(
                withId(R.id.QlineCheckBox),
                withText("Qline")
            )
        )
            .check(matches(isDisplayed()))

        onView(allOf(withId(R.id.DdotCheckBox), isDisplayed()))
            .check(matches(isNotChecked()))
            .perform(click())
            .check(matches(isChecked()))
            .perform(click())
            .check(matches(isNotChecked()))

        onView(allOf(withId(R.id.SmartCheckBox), isDisplayed()))
            .check(matches(isNotChecked()))
            .perform(click())
            .check(matches(isChecked()))
            .perform(click())
            .check(matches(isNotChecked()))

        onView(allOf(withId(R.id.ReflexCheckBox), isDisplayed()))
            .check(matches(isNotChecked()))
            .perform(click())
            .check(matches(isChecked()))
            .perform(click())
            .check(matches(isNotChecked()))

        onView(allOf(withId(R.id.PeopleMoverCheckBox), isDisplayed()))
            .check(matches(isNotChecked()))
            .perform(click())
            .check(matches(isChecked()))
            .perform(click())
            .check(matches(isNotChecked()))

        onView(allOf(withId(R.id.QlineCheckBox), isDisplayed()))
            .check(matches(isNotChecked()))
            .perform(click())
            .check(matches(isChecked()))
            .perform(click())
            .check(matches(isNotChecked()))
    }

}