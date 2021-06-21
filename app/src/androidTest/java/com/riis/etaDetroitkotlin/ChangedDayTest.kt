package com.riis.etaDetroitkotlin


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
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
 * Tests the functionality of the day tabs within the bus routes. It verifies the WEEKDAY tab is
 * selected by default, then it clicks each tab and verifies that they are selected.
 *
 *  Created by csollars 3/4/19
 *
 */

@RunWith(AndroidJUnit4::class)
@LargeTest
class ChangeDayTest {

    @get:Rule
    var mActivityTestRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun changeDayTest() {

        // click smart bus
        onView(allOf(withId(R.id.company_name), withText("SmartBus"), isDisplayed()))
            .perform(click())


        // click route 125
        onView(allOf(withId(R.id.route_number), withText("ROUTE 125")))
            .perform(click())

        // verify the weekday tab is selected by default
        onView(allOf(withText("WEEKDAY"), isDisplayed()))
            .check(matches(isSelected()))

        // click saturday tab
        onView(allOf(withText("SATURDAY"), isDisplayed()))
            .perform(click())

        // verify saturday tab is selected
        onView(allOf(withText("SATURDAY"), isDisplayed()))
            .check(matches(isSelected()))

        // click sunday tab
        onView(allOf(withText("SUNDAY"), isDisplayed()))
            .perform(click())

        // verify sunday tab is selected
        onView(allOf(withText("SUNDAY"), isDisplayed()))
            .check(matches(isSelected()))

        // click weekday tab
        onView(allOf(allOf(withText("WEEKDAY")), isDisplayed()))
            .perform(click())

        // verify weekday tab is selected
        onView(allOf(allOf(withText("WEEKDAY")), isDisplayed()))
            .check(matches(isSelected()))

        // navigate back to homepage
        pressBack()
        pressBack()

        // click ddot
        onView(allOf(withId(R.id.company_name), withText("DDOT"), isDisplayed()))
            .perform(click())

        // click route 001
        onView(allOf(withId(R.id.route_number), withText("ROUTE 1")))
            .perform(click())

        // verify the weekday tab is selected by default
        onView(allOf(withText("WEEKDAY"), isDisplayed()))
        .check(matches(isSelected()))

        // click saturday tab
        onView(allOf(withText("SATURDAY"), isDisplayed()))
        .perform(click())

        // verify saturday tab is selected
       onView(allOf(withText("SATURDAY"), isDisplayed()))
        .check(matches(isSelected()))

        // click sunday tab
        onView(allOf(withText("SUNDAY"), isDisplayed()))
        .perform(click())

        // verify sunday tab is selected
        onView(allOf(withText("SUNDAY"), isDisplayed()))
        .check(matches(isSelected()))

        // click weekday tab
        onView(allOf(withText("WEEKDAY"), isDisplayed()))
        .perform(click())

        // verify weekday tab is selected
        onView(allOf(withText("WEEKDAY"), isDisplayed()))
        .check(matches(isSelected()))

        // navigate back to homepage
        pressBack()
        pressBack()

        // click smart bus
        onView(allOf(withId(R.id.company_name), withText("FAST"), isDisplayed()))
        .perform(click())

        // click route 125
        onView(allOf(withId(R.id.route_number), withText("ROUTE 461")))
        .perform(click())

        // verify the weekday tab is selected by default
        onView(allOf(withText("WEEKDAY"), isDisplayed()))
        .check(matches(isSelected()))

        // click saturday tab
        onView(allOf(withText("SATURDAY"), isDisplayed()))
        .perform(click())

        // verify saturday tab is selected
        onView(allOf(withText("SATURDAY"), isDisplayed()))
        .check(matches(isSelected()))

        // click sunday tab
        onView(allOf(withText("SUNDAY"), isDisplayed()))
        .perform(click())

        // verify sunday tab is selected
        onView(allOf(withText("SUNDAY"), isDisplayed()))
        .check(matches(isSelected()))

        // click weekday tab
        onView(allOf(withText("WEEKDAY"), isDisplayed()))
        .perform(click())

        // verify weekday tab is selected
        onView(allOf(withText("WEEKDAY"), isDisplayed()))
        .check(matches(isSelected()))
    }
}
