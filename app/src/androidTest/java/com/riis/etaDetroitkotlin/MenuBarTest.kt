package com.riis.etaDetroitkotlin

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Matchers.allOf
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MenuBarTest {

    @get: Rule
    var mActivityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Before
    fun testOpenNavigation() {
        val appCompatImageButton: ViewInteraction = onView(
            allOf(withContentDescription("Open navigation drawer"), isDisplayed())
        )
        appCompatImageButton.perform(click())
    }

    @Test
    fun testMenuHome() {
        val homeTextView: ViewInteraction = onView(
            allOf(withId(R.id.design_menu_item_text), withText("Home"), isDisplayed())
        )
        homeTextView.check(matches(isDisplayed()))
    }

    @Test
    fun testMenuPlanMyRoute() {
        val routePlannerTextView: ViewInteraction = onView(
            allOf(
                withId(R.id.design_menu_item_text), withText("Plan My Route"),
                isDisplayed()
            )
        )
        routePlannerTextView.check(matches(isDisplayed()))
    }

    @Test
    fun testMenuRouteMap() {
        val routeMapTextView: ViewInteraction = onView(
            allOf(withId(R.id.design_menu_item_text), withText("Route Map"), isDisplayed())
        )
        routeMapTextView.check(matches(isDisplayed()))
    }

    @Test
    fun testBusRoutesLabel() {
        val busRoutesLabel: ViewInteraction = onView(
            allOf(withText("Bus routes"), isDisplayed())
        )
        busRoutesLabel.check(matches(isDisplayed()))
    }

    @Test
    fun testMenuDDOTBus() {
        val ddotBusTextView: ViewInteraction = onView(
            allOf(withId(R.id.design_menu_item_text), withText("DDOT Bus"), isDisplayed())
        )
        ddotBusTextView.check(matches(isDisplayed()))
    }

    @Test
    fun testMenuSmartBus() {
        val smartBusTextView: ViewInteraction = onView(
            allOf(withId(R.id.design_menu_item_text), withText("Smart Bus"), isDisplayed())
        )
        smartBusTextView.check(matches(isDisplayed()))
    }

    @Test
    fun testMenuFastBus() {
        val fastBusTextView: ViewInteraction = onView(
            allOf(withId(R.id.design_menu_item_text), withText("Fast Bus"), isDisplayed())
        )
        fastBusTextView.check(matches(isDisplayed()))
    }

    @Test
    fun testMenuPeopleMover() {
        val peopleMoverTextView: ViewInteraction = onView(
            allOf(
                withId(R.id.design_menu_item_text), withText("Detroit People Mover"),
                isDisplayed()
            )
        )
        peopleMoverTextView.check(matches(isDisplayed()))
    }

    @Test
    fun testMenuQline() {
        val qlineTextView: ViewInteraction = onView(
            allOf(withId(R.id.design_menu_item_text), withText("Qline"), isDisplayed())
        )
        qlineTextView.check(matches(isDisplayed()))
    }


}