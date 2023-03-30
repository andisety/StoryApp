package com.andi.storyapp.network


import android.content.Context
import android.util.Log
import com.andi.storyapp.network.ApiService
import com.andi.storyapp.BuildConfig
import com.andi.storyapp.model.SavePrefObject
import com.andi.storyapp.model.Savepref
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class ApiConfig {

    companion object{
        private fun getInterceptor(token: String?): OkHttpClient {
            val loggingInterceptor = if(BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            } else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }

            return if (token.isNullOrEmpty()) {
                OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build()
            } else {
                Log.e("TOKEN-API_CONFIG",token)
                OkHttpClient.Builder()
                    .addInterceptor(InterceptorAuth(token))
                    .addInterceptor(loggingInterceptor)
                    .build()
            }
        }

        fun getApiService(context: Context): ApiService {
            val sharedPref = SavePrefObject.initPref(context)
            val token = sharedPref.getString(Savepref.TOKEN, null)
            val retrofit = Retrofit.Builder()
                .baseUrl("https://story-api.dicoding.dev/v1/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(getInterceptor(token))
                .build()

            return retrofit.create(ApiService::class.java)

        }
    }
}