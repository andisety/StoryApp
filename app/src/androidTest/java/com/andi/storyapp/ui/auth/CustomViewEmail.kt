package com.andi.storyapp.ui.auth

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers.isAssignableFrom
import com.andi.storyapp.ui.customui.EmailEditText
import org.hamcrest.Matcher


class CustomViewEmail(private val email:String): ViewAction {
    override fun getDescription(): String {
        return "Email"
    }

    override fun getConstraints(): Matcher<View> {
       return  isAssignableFrom(EmailEditText::class.java)
    }

    override fun perform(uiController: UiController?, view: View?) {
        val custom = view as EmailEditText
        custom.setText(email)
    }
}