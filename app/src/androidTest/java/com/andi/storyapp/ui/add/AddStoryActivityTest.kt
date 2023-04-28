package com.andi.storyapp.ui.add

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.andi.storyapp.EspressoIdlingResource
import com.andi.storyapp.R
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class AddStoryActivityTest{
    @get:Rule
    val activity = ActivityScenarioRule(AddStoryActivity::class.java)
    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }
    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }
    @Test
    fun upload(){
        onView(withId(R.id.btnCamera)).perform(click())
        onView(withId(R.id.captureImage)).check(matches(isDisplayed()))
        Thread.sleep(2000)
        onView(withId(R.id.captureImage)).perform(click())
        Thread.sleep(5000)
        onView(withId(R.id.etDesc)).perform(typeText("description"), closeSoftKeyboard())
        onView(withId(R.id.btnUpload)).perform(click())
        Thread.sleep(3000)

    }
}