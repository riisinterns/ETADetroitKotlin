package com.riis.etaDetroitkotlin

import android.view.View
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.Espresso.pressBack
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
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

@LargeTest
@RunWith(AndroidJUnit4::class)
class DirectionChangeTest {
    @get:Rule
    var mActivityScenarioRule: ActivityScenarioRule<MainActivity> =
        ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun directionChangeTest() {
//        try {
//            Thread.sleep(5000)
//        } catch (e: InterruptedException) {
//            e.printStackTrace()
//        }

        val linearLayout = onView(
            allOf(
                withId(R.id.company_name), withText("SmartBus"),
                isDisplayed()
            )
        )
        linearLayout.perform(click())
        val recyclerView = onView(allOf(withId(R.id.route_number), withText("125")))
        recyclerView.perform(click())

        // locate the first stop
        val textView9 = onView(allOf(withIndex(withId(R.id.base_layout), 0)))
        textView9.check(matches(isDisplayed()))

        // click the arrow icon
        val floatingActionButton = onView(allOf(withId(R.id.fab), isDisplayed()))
        floatingActionButton.perform(click())

        // veify the first stop is no longer visible on the screen
        textView9.check(ViewAssertions.doesNotExist())
        pressBack()
        pressBack()
        val linearLayout1 = onView(
            allOf(withId(R.id.company_name), withText("DDOT"), isDisplayed())
        )
        linearLayout1.perform(click())
        val recyclerView2 = onView(allOf(withId(R.id.route_number), withText("001")))
        recyclerView2.perform(click())

        // locate the first stop
        val textView = onView(allOf(withIndex(withId(R.id.base_layout), 0)))
        textView.check(matches(isDisplayed()))

        // click the arrow icon
        val floatingActionButton2 = onView(allOf(withId(R.id.fab), isDisplayed()))
        floatingActionButton2.perform(click())

        // verify the first stop is no longer visible on screen
        textView.check(ViewAssertions.doesNotExist())
        pressBack()
        pressBack()
        val linearLayout3 = onView(
            allOf(
                withId(R.id.company_name), withText("FAST"),
                isDisplayed()
            )
        )
        linearLayout3.perform(click())
        val recyclerView3 = onView(allOf(withId(R.id.route_number), withText("461")))
        recyclerView3.perform(click())
        val textView3 = onView(allOf(withIndex(withId(R.id.base_layout), 0)))
        textView3.check(matches(isDisplayed()))
        val floatingActionButton3 = onView(allOf(withId(R.id.fab), isDisplayed()))
        floatingActionButton3.perform(click())
        textView3.check(ViewAssertions.doesNotExist())
    }

    companion object {
        fun withIndex(matcher: Matcher<View?>, index: Int): TypeSafeMatcher<View?> {
            return object : TypeSafeMatcher<View?>() {
                var currentIndex = 0
                override fun describeTo(description: Description) {
                    description.appendText("with index: ")
                    description.appendValue(index)
                    matcher.describeTo(description)
                }

                public override fun matchesSafely(view: View?): Boolean {
                    return matcher.matches(view) && currentIndex++ == index
                }
            }
        }
    }
}

