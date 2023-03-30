package com.andi.storyapp.model

import android.content.Context
import android.content.SharedPreferences

class Savepref {

    companion object {
        val PREF_NAME = "storyApp"
        val TOKEN = "TOKEN"
    }

}

object SavePrefObject{
    fun initPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(Savepref.PREF_NAME, Context.MODE_PRIVATE)
    }
     fun editorPreference(context: Context): SharedPreferences.Editor {
        val sharedPref = context.getSharedPreferences(Savepref.PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.edit()
    }
    fun setToken(token: String, context: Context) {
        val editor = editorPreference(context)
        editor.putString(Savepref.TOKEN, token)
        editor.apply()
    }

}