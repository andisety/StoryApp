package com.andi.storyapp.ui.auth


import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.andi.storyapp.EspressoIdlingResource
import com.andi.storyapp.R
import org.hamcrest.Matchers
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


@RunWith(AndroidJUnit4::class)
@LargeTest
class LoginActivityTest{
    private val email = "razin69@gmail.com"
    private val pwd = "12345678"
    @get:Rule
    val activity = ActivityScenarioRule(LoginActivity::class.java)
    @Before
    fun setUp() {
        IdlingRegistry.getInstance().register(EspressoIdlingResource.countingIdlingResource)
    }
    @After
    fun tearDown() {
        IdlingRegistry.getInstance().unregister(EspressoIdlingResource.countingIdlingResource)
    }
    @Test
    fun loginTest() {
        onView(
            Matchers.allOf(
                isDescendantOfA(withId(R.id.etEmail)),
                withClassName(Matchers.endsWith("EditText")),
                withId(R.id.etEmail)
            )
        ).perform(typeText(email), closeSoftKeyboard())

        onView(
            Matchers.allOf(
                withClassName(Matchers.endsWith("PasswordEditText")),
                withId(R.id.etPwd)
            )
        ).perform(typeText(pwd))

        onView(withId(R.id.btnLogin)).perform(click())
    }

}