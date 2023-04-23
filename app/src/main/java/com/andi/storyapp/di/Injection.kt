package com.andi.storyapp.di

import android.content.Context
import com.andi.storyapp.data.StoryRepository
import com.andi.storyapp.database.StoryDataBase
import com.andi.storyapp.network.ApiConfig
import com.andi.storyapp.preference.PreferenceLogin

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val pref = PreferenceLogin(context)
        val token= pref.getToken().toString()
        val database = StoryDataBase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(database, apiService,token)
    }
}