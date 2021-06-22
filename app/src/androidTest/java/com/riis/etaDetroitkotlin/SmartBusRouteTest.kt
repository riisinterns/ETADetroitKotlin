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

@RunWith(AndroidJUnit4::class)
class SmartBusRouteTest {

    @get:Rule
    var mActivityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun smartBusRouteTest() {

        onView(allOf(withId(R.id.company_name), withText("SmartBus"), isDisplayed()))
            .perform(click())

        onView(allOf(withId(R.id.route_name), withText("FORT ST-EUREKA RD"), isDisplayed()))
            .check(matches(withText("FORT ST-EUREKA RD")))

        onView(allOf(withId(R.id.route_name), withText("SOUTHSHORE"), isDisplayed()))
            .check(matches(withText("SOUTHSHORE")))

        onView(allOf(withId(R.id.route_name), withText("DOWNRIVER"), isDisplayed()))
            .check(matches(withText("DOWNRIVER")))

        onView(allOf(withId(R.id.route_name), withText("MICHIGAN AVENUE LOCAL"), isDisplayed()))
            .check(matches(withText("MICHIGAN AVENUE LOCAL")))

        onView(allOf(withId(R.id.route_name), withText("FORD RD"), isDisplayed()))
            .check(matches(withText("FORD RD")))
    }
}